package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao

class AchievementsViewModelFactory(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            return AchievementsViewModel(singularAchievementDao, userAchievementDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
