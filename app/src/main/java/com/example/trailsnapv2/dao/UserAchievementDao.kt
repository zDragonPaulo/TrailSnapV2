package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement

@Dao
interface UserAchievementDao {

    @Insert
    suspend fun insert(userAchievement: UserAchievement)

    @Insert
    suspend fun insertAll(userAchievements: List<UserAchievement>)

    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    suspend fun getUserAchievements(userId: Long): List<UserAchievement>

    @Update
    suspend fun updateAll(userAchievements: List<UserAchievement>)
    @Query("SELECT * FROM user_achievements WHERE user_id = :userId AND progress < 100 ORDER BY progress DESC LIMIT 3")
    suspend fun getTopProgressAchievements(userId: Long): List<UserAchievement>
    @Query("SELECT * FROM singular_achievements WHERE id_achievement IN (SELECT achievement_id FROM user_achievements WHERE user_id = :userId)")
    suspend fun getSingularAchievementsForUser(userId: Long): List<SingularAchievement>


}
