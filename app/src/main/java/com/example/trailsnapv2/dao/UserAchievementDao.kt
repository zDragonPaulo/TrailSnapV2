package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.UserAchievement

@Dao
interface UserAchievementDao {

    @Insert
    suspend fun insert(userAchievement: UserAchievement)

    @Insert
    suspend fun insertAll(userAchievements: List<UserAchievement>)

    @Update
    suspend fun update(userAchievement: UserAchievement)

    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    suspend fun getUserAchievements(userId: Long): List<UserAchievement>

    @Query("UPDATE user_achievements SET progress = :progress, unlocked = :unlocked WHERE id_user_achievement = :id")
    suspend fun updateProgress(id: Long, progress: Double, unlocked: Boolean)

    @Query("DELETE FROM user_achievements WHERE id_user_achievement = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM user_achievements WHERE user_id = :userId")
    suspend fun deleteByUserId(userId: Long)
}
