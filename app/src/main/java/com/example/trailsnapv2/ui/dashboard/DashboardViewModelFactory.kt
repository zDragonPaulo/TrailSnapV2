package com.example.trailsnapv2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao

class DashboardViewModelFactory(
    private val userDao: UserDao,
    private val walkDao: WalkDao,
    private val userAchievementDao: UserAchievementDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(userDao, walkDao, userAchievementDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
