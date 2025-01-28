package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.WalkDao

/**
 * A factory class for creating instances of the [AchievementsViewModel] with the required dependencies.
 * This class ensures that the ViewModel has the necessary DAO objects to interact with the database.
 */
class AchievementsViewModelFactory(
    private val singularAchievementDao: SingularAchievementDao,
    private val userAchievementDao: UserAchievementDao,
    private val walkDao: WalkDao
) : ViewModelProvider.Factory {

    /**
     * Creates and returns an instance of [AchievementsViewModel] with the required dependencies.
     *
     * @param modelClass The class type of the ViewModel that needs to be created.
     * @return A new instance of [AchievementsViewModel] with the provided DAOs injected.
     * @throws IllegalArgumentException If the provided model class is not assignable to [AchievementsViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            return AchievementsViewModel(singularAchievementDao, userAchievementDao, walkDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
