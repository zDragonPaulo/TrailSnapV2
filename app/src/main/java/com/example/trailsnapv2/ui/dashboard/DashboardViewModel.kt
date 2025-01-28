package com.example.trailsnapv2.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.User
import com.example.trailsnapv2.entities.UserAchievement
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * ViewModel for the Dashboard UI, responsible for loading user data,
 * tracking user achievements, and handling user-related metrics.
 * This ViewModel manages the data for the user's profile, achievements, and walk statistics.
 *
 * @param userDao The Data Access Object (DAO) for accessing user data.
 * @param walkDao The Data Access Object (DAO) for accessing walk-related data.
 * @param userAchievementDao The Data Access Object (DAO) for accessing user achievements data.
 */
class DashboardViewModel(
    private val userDao: UserDao,
    private val walkDao: WalkDao,
    private val userAchievementDao: UserAchievementDao
) : ViewModel() {

    private val _userAchievements = MutableLiveData<List<UserAchievement>>()
    val userAchievements: LiveData<List<UserAchievement>> = _userAchievements

    private val _singularAchievements = MutableLiveData<List<SingularAchievement>>()
    val singularAchievements: LiveData<List<SingularAchievement>> = _singularAchievements

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _totalWalks = MutableLiveData<Int>()
    val totalWalks: LiveData<Int> = _totalWalks

    /**
     * Loads the user data from the database and updates the LiveData.
     * This includes the user's profile and statistics.
     *
     * @param userId The ID of the user to load the data for.
     */
    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            val user = userDao.getUserById(userId).firstOrNull()
            if (user != null) {
                _userData.postValue(user!!)
            }
        }
    }

    /**
     * Loads the total number of walks completed by the user and updates the LiveData.
     *
     * @param userId The ID of the user to calculate the total walks for.
     */
    fun loadTotalWalks(userId: Long) {
        viewModelScope.launch {
            val total = walkDao.calculateWalksCompleted(userId)
            _totalWalks.postValue(total)
        }
    }

    /**
     * Loads the user's top achievements based on their progress and updates the LiveData.
     * This includes both user-specific achievements and corresponding singular achievements.
     *
     * @param userId The ID of the user to load the achievements for.
     */
    fun loadTopAchievements(userId: Long) {
        viewModelScope.launch {
            val topUserAchievements = userAchievementDao.getTopProgressAchievements(userId)
            _userAchievements.postValue(topUserAchievements)

            val singularAchievementsList = userAchievementDao.getSingularAchievementsForUser(userId)
            _singularAchievements.postValue(singularAchievementsList)
        }
    }
}

