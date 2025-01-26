package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.WalkDao

class AchievementsViewModelFactory(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao,
    private val walkDao: WalkDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            return AchievementsViewModel(singularAchievementDao, userAchievementDao, walkDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
