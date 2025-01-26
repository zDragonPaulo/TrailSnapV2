package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement
import kotlinx.coroutines.launch
import org.json.JSONObject

class AchievementsViewModel(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao,
    private val walkDao: WalkDao
) : ViewModel() {

    fun insertDefaultAchievementsIfNeeded() {
        viewModelScope.launch {
            // Check if achievements are already initialized
            if (singularAchievementDao.getAll().isEmpty()) {
                val defaultAchievements = createDefaultAchievements()
                singularAchievementDao.insertAll(defaultAchievements)
            }
        }
    }

    // Returns a list of default achievements
    private fun createDefaultAchievements(): List<SingularAchievement> {
        return listOf(
            SingularAchievement(0, "First Steps", "Walked 1 kilometer", """{"metric": "distance_walked", "target": 1.0}"""),
            SingularAchievement(0, "First 5K", "Walked 5 kilometers", """{"metric": "distance_walked", "target": 5.0}"""),
            SingularAchievement(0, "10K Milestone", "Walked 10 kilometers", """{"metric": "distance_walked", "target": 10.0}"""),
            SingularAchievement(0, "Half Marathon", "Walked 25 kilometers", """{"metric": "distance_walked", "target": 25.0}"""),
            SingularAchievement(0, "Full Marathon", "Walked 50 kilometers", """{"metric": "distance_walked", "target": 50.0}"""),
            SingularAchievement(0, "Century Walker", "Walked 100 kilometers", """{"metric": "distance_walked", "target": 100.0}"""),
            SingularAchievement(0, "Super Walker", "Walked 250 kilometers", """{"metric": "distance_walked", "target": 250.0}"""),
            SingularAchievement(0, "Explorer", "Walked 500 kilometers", """{"metric": "distance_walked", "target": 500.0}"""),
            SingularAchievement(0, "Walking Pro", "Walked 1000 kilometers", """{"metric": "distance_walked", "target": 1000.0}"""),
            SingularAchievement(0, "First Walk", "Completed your first walk", """{"metric": "walks_completed", "target": 1.0}"""),
            SingularAchievement(0, "First 5W", "Completed 5 walks", """{"metric": "walks_completed", "target": 5.0}"""),
            SingularAchievement(0, "10W Milestone", "Completed 10 walks", """{"metric": "walks_completed", "target": 10.0}"""),
            SingularAchievement(0, "25 Walks Challenge", "Completed 25 walks", """{"metric": "walks_completed", "target": 25.0}"""),
            SingularAchievement(0, "Walking Enthusiast", "Completed 50 walks", """{"metric": "walks_completed", "target": 50.0}"""),
            SingularAchievement(0, "Walking Fanatic", "Completed 100 walks", """{"metric": "walks_completed", "target": 100.0}"""),
            SingularAchievement(0, "Walking Maniac", "Completed 250 walks", """{"metric": "walks_completed", "target": 250.0}"""),
            SingularAchievement(0, "Walking Machine", "Completed 500 walks", """{"metric": "walks_completed", "target": 500.0}"""),
            SingularAchievement(0, "Walking Legend", "Completed 1000 walks", """{"metric": "walks_completed", "target": 1000.0}""")
        )
    }

    fun updateAchievements(userId: Long) {
        viewModelScope.launch {
            // Get all singular achievements
            val singularAchievements = singularAchievementDao.getAll()

            // Get the user's current achievements
            val userAchievements = userAchievementDao.getUserAchievements(userId)

            // Fetch user's metrics
            val distanceWalked = getTotalDistanceWalked(userId) // Get total distance
            val walksCompleted = getTotalWalksCompleted(userId) // Get total walks

            // Loop through the user's achievements and update their progress
            val updatedAchievements = userAchievements.map { userAchievement ->
                val singularAchievement = singularAchievements.find { it.id_achievement == userAchievement.achievement_id }
                singularAchievement?.let { sa ->
                    val condition = sa.condition // Get condition JSON string
                    val (metric, target) = parseCondition(condition) // Parse the condition into metric and target

                    // Calculate progress based on the metric
                    val progress = when (metric) {
                        "distance_walked" -> (distanceWalked / target) * 100
                        "walks_completed" -> (walksCompleted / target) * 100
                        else -> userAchievement.progress  // Fallback to existing progress
                    }

                    // Determine if the achievement is unlocked
                    val unlocked = progress >= 100

                    userAchievement.copy(progress = progress.coerceAtMost(100.0), unlocked = unlocked)
                } ?: userAchievement
            }

            // Update the user's achievements in the database
            userAchievementDao.updateAll(updatedAchievements)
        }
    }

    // Function to get total distance walked for a user
    private suspend fun getTotalDistanceWalked(userId: Long): Double {
        return walkDao.calculateDistanceWalked(userId) // Ensure this function returns a Double
    }

    // Function to get total walks completed by a user
    private suspend fun getTotalWalksCompleted(userId: Long): Int {
        return walkDao.calculateWalksCompleted(userId) // Ensure this function returns an Int
    }


    // Helper function to parse the condition JSON string
    private fun parseCondition(condition: String): Pair<String, Double> {
        val json = JSONObject(condition)
        val metric = json.getString("metric")
        val target = json.getDouble("target")
        return Pair(metric, target)
    }


    // Function to get all SingularAchievements
    fun getAllSingularAchievements(): LiveData<List<SingularAchievement>> {
        return liveData {
            emit(singularAchievementDao.getAll())
        }
    }

    // Function to get UserAchievements
    fun getUserAchievements(userId: Long): LiveData<List<UserAchievement>> {
        return liveData {
            emit(userAchievementDao.getUserAchievements(userId))
        }
    }

    // Function to initialize user achievements
    fun initializeUserAchievements(userId: Long) {
        viewModelScope.launch {
            // Check if user achievements are initialized for this user
            if (userAchievementDao.getUserAchievements(userId).isEmpty()) {
                val achievements = singularAchievementDao.getAll()
                val userAchievements = achievements.map { achievement ->
                    UserAchievement(0, userId, achievement.id_achievement, 0.0, false)
                }
                userAchievementDao.insertAll(userAchievements)
            }
            updateAchievements(userId)
        }
    }
}
