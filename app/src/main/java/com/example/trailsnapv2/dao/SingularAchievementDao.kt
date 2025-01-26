package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.SingularAchievement

@Dao
interface SingularAchievementDao {

    @Insert
    suspend fun insert(achievement: SingularAchievement)

    @Insert
    suspend fun insertAll(achievements: List<SingularAchievement>)

    @Query("SELECT * FROM singular_achievements")
    suspend fun getAll(): List<SingularAchievement>

    @Query("SELECT * FROM singular_achievements WHERE id_achievement = :achievementId")
    suspend fun getById(achievementId: Long): SingularAchievement?

    @Query("DELETE FROM singular_achievements WHERE id_achievement = :achievementId")
    suspend fun deleteById(achievementId: Long)

    @Query("DELETE FROM singular_achievements")
    suspend fun clearAll()
}
