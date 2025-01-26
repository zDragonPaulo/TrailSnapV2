package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao
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
            SingularAchievement(0, "First Steps", "Walked 1 kilometer"),
            SingularAchievement(0, "First 5K", "Walked 5 kilometers"),
            SingularAchievement(0, "10K Milestone", "Walked 10 kilometers"),
            SingularAchievement(0, "Half Marathon", "Walked 25 kilometers"),
            SingularAchievement(0, "Full Marathon", "Walked 50 kilometers"),
            SingularAchievement(0, "Century Walker", "Walked 100 kilometers"),
            SingularAchievement(0, "Super Walker", "Walked 250 kilometers"),
            SingularAchievement(0, "Explorer", "Walked 500 kilometers"),
            SingularAchievement(0, "Walking Pro", "Walked 1000 kilometers"),
            SingularAchievement(0, "First Walk", "Completed your first walk"),
            SingularAchievement(0, "First 5W", "Completed 5 walks"),
            SingularAchievement(0, "10W Milestone", "Completed 10 walks"),
            SingularAchievement(0, "25 Walks Challenge", "Completed 25 walks"),
            SingularAchievement(0, "Walking Enthusiast", "Completed 50 walks"),
            SingularAchievement(0, "Walking Fanatic", "Completed 100 walks"),
            SingularAchievement(0, "Walking Maniac", "Completed 250 walks"),
            SingularAchievement(0, "Walking Machine", "Completed 500 walks"),
            SingularAchievement(0, "Walking Legend", "Completed 1000 walks")
        )
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
        }
    }
}
