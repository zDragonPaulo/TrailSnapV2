package com.example.trailsnapv2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao

/**
 * Factory class for creating instances of [DashboardViewModel].
 * This factory ensures that the required dependencies (UserDao, WalkDao, UserAchievementDao) are provided
 * when creating a new instance of [DashboardViewModel].
 */
class DashboardViewModelFactory(
    private val userDao: UserDao,
    private val walkDao: WalkDao,
    private val userAchievementDao: UserAchievementDao
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of [DashboardViewModel] with the provided dependencies.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return An instance of [DashboardViewModel].
     * @throws IllegalArgumentException if the [modelClass] is not assignable from [DashboardViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(userDao, walkDao, userAchievementDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
