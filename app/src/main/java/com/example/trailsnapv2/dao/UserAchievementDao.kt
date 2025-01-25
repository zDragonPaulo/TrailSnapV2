package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.UserAchievement

@Dao
interface UserAchievementDao {

    @Query("SELECT * FROM user_achievements")
    fun getAll(): List<UserAchievement>

    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    fun getByUserId(userId: Long): List<UserAchievement>

    @Insert
    fun insert(userAchievement: UserAchievement): Long

    @Delete
    fun delete(userAchievement: UserAchievement): Int

    @Insert
    fun insertAll(vararg userAchievements: UserAchievement): List<Long>
}