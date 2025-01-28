package com.example.trailsnapv2.ui.achievements

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.R
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * ViewModel responsible for handling and managing achievements for the user.
 * It interacts with the DAO layers to retrieve and update achievements based on the user's progress.
 * It ensures that default achievements are inserted if necessary and updates the user's achievements.
 */
class AchievementsViewModel(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao,
    private val walkDao: WalkDao
) : ViewModel() {

    /**
     * Inserts default achievements into the database if they haven't been inserted already.
     * This ensures that the necessary achievements are available for all users.
     */
    fun insertDefaultAchievementsIfNeeded(context: Context) {
        viewModelScope.launch {
            if (singularAchievementDao.getAll().isEmpty()) {
                val defaultAchievements = createDefaultAchievements(context)
                singularAchievementDao.insertAll(defaultAchievements)
            }
        }
    }

    /**
     * Creates a list of default achievements that can be unlocked by users.
     *
     * @return A list of default SingularAchievements.
     */
    private fun createDefaultAchievements(context: Context): List<SingularAchievement> {
        return listOf(
            SingularAchievement(
                0,
                context.getString(R.string.achievement_first_kilometer),
                context.getString(R.string.achievement_first_kilometer_desc),
                """{"metric": "distance_walked", "target": 1000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_first_5k),
                context.getString(R.string.achievement_first_5k_desc),
                """{"metric": "distance_walked", "target": 5000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_10k_milestone),
                context.getString(R.string.achievement_10k_milestone_desc),
                """{"metric": "distance_walked", "target": 10000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_half_marathon),
                context.getString(R.string.achievement_half_marathon_desc),
                """{"metric": "distance_walked", "target": 25000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_full_marathon),
                context.getString(R.string.achievement_full_marathon_desc),
                """{"metric": "distance_walked", "target": 50000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_century_walker),
                context.getString(R.string.achievement_century_walker_desc),
                """{"metric": "distance_walked", "target": 100000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_super_walker),
                context.getString(R.string.achievement_super_walker_desc),
                """{"metric": "distance_walked", "target": 250000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_explorer),
                context.getString(R.string.achievement_explorer_desc),
                """{"metric": "distance_walked", "target": 500000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_pro),
                context.getString(R.string.achievement_walking_pro_desc),
                """{"metric": "distance_walked", "target": 1000000.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_first_walk),
                context.getString(R.string.achievement_first_walk_desc),
                """{"metric": "walks_completed", "target": 1.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_first_5w),
                context.getString(R.string.achievement_first_5w_desc),
                """{"metric": "walks_completed", "target": 5.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_10w_milestone),
                context.getString(R.string.achievement_10w_milestone_desc),
                """{"metric": "walks_completed", "target": 10.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_25_walks),
                context.getString(R.string.achievement_25_walks_desc),
                """{"metric": "walks_completed", "target": 25.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_enthusiast),
                context.getString(R.string.achievement_walking_enthusiast_desc),
                """{"metric": "walks_completed", "target": 50.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_fanatic),
                context.getString(R.string.achievement_walking_fanatic_desc),
                """{"metric": "walks_completed", "target": 100.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_maniac),
                context.getString(R.string.achievement_walking_maniac_desc),
                """{"metric": "walks_completed", "target": 250.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_machine),
                context.getString(R.string.achievement_walking_machine_desc),
                """{"metric": "walks_completed", "target": 500.0}"""
            ),
            SingularAchievement(
                0,
                context.getString(R.string.achievement_walking_legend),
                context.getString(R.string.achievement_walking_legend_desc),
                """{"metric": "walks_completed", "target": 1000.0}"""
            )
        )
    }


    /**
     * Updates the user's achievements based on their progress. This includes calculating the user's
     * total distance walked and total walks completed, and updating the user's achievements accordingly.
     *
     * @param userId The ID of the user whose achievements will be updated.
     */
    fun updateAchievements(userId: Long) {
        viewModelScope.launch {
            val singularAchievements = singularAchievementDao.getAll()
            val userAchievements = userAchievementDao.getUserAchievements(userId)

            if (getTotalDistanceWalked(userId) != null) {
                val distanceWalked = getTotalDistanceWalked(userId)
                val walksCompleted = getTotalWalksCompleted(userId)

                val updatedAchievements = userAchievements.map { userAchievement ->
                    val singularAchievement = singularAchievements.find { it.id_achievement == userAchievement.achievement_id }
                    singularAchievement?.let { sa ->
                        val condition = sa.condition
                        val (metric, target) = parseCondition(condition)

                        val progress = when (metric) {
                            "distance_walked" -> (distanceWalked / target) * 100
                            "walks_completed" -> (walksCompleted / target) * 100
                            else -> userAchievement.progress
                        }

                        val unlocked = progress >= 100

                        userAchievement.copy(progress = progress.coerceAtMost(100.0), unlocked = unlocked)
                    } ?: userAchievement
                }

                userAchievementDao.updateAll(updatedAchievements)

            } else {
                Log.e("AchievementsViewModel", "Failed to retrieve total distance walked for user $userId")

            }
        }
    }

    /**
     * Retrieves the total distance walked by the user.
     *
     * @param userId The ID of the user.
     * @return The total distance walked by the user.
     */
    private suspend fun getTotalDistanceWalked(userId: Long): Double {
        return walkDao.calculateDistanceWalked(userId)
    }

    /**
     * Retrieves the total number of walks completed by the user.
     *
     * @param userId The ID of the user.
     * @return The total number of walks completed by the user.
     */
    private suspend fun getTotalWalksCompleted(userId: Long): Int {
        return walkDao.calculateWalksCompleted(userId)
    }

    /**
     * Parses the condition of an achievement from its JSON string to extract the metric and target.
     *
     * @param condition The JSON string representing the condition of the achievement.
     * @return A pair consisting of the metric type and target value.
     */
    private fun parseCondition(condition: String): Pair<String, Double> {
        val json = JSONObject(condition)
        val metric = json.getString("metric")
        val target = json.getDouble("target")
        return Pair(metric, target)
    }

    /**
     * Retrieves all singular achievements from the database.
     *
     * @return A LiveData object containing a list of all SingularAchievements.
     */
    fun getAllSingularAchievements(): LiveData<List<SingularAchievement>> {
        return liveData {
            emit(singularAchievementDao.getAll())
        }
    }

    /**
     * Retrieves the user achievements for a specific user.
     *
     * @param userId The ID of the user.
     * @return A LiveData object containing a list of the user's achievements.
     */
    fun getUserAchievements(userId: Long): LiveData<List<UserAchievement>> {
        return liveData {
            emit(userAchievementDao.getUserAchievements(userId))
        }
    }

    /**
     * Initializes the user's achievements. If the user does not have any achievements,
     * it creates and inserts them based on the available singular achievements.
     *
     * @param userId The ID of the user whose achievements will be initialized.
     */
    fun initializeUserAchievements(userId: Long) {
        viewModelScope.launch {
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
