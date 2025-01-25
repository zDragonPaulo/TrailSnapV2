package com.example.trailsnapv2.ui.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.entities.SingularAchievement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementsViewModel(private val singularAchievementDao: SingularAchievementDao) : ViewModel() {

    // Insere uma nova conquista na tabela SingularAchievement
    fun insertAchievement(achievement: SingularAchievement) {
        viewModelScope.launch(Dispatchers.IO) {
            singularAchievementDao.insert(achievement)
        }
    }

    // Obtém todas as conquistas
    fun getAllAchievements(): List<SingularAchievement> {
        return singularAchievementDao.getAll()
    }

    fun clearAllAchievements() {
        viewModelScope.launch(Dispatchers.IO) {
            singularAchievementDao.clearAll()
        }
    }
}
