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

    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    suspend fun getUserAchievements(userId: Long): List<UserAchievement>

    @Update
    suspend fun updateAll(userAchievements: List<UserAchievement>)

}
