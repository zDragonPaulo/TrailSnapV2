package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.SingularAchievement

@Dao
interface SingularAchievementDao {
    @Query("SELECT * FROM singular_achievements")
    fun getAll(): List<SingularAchievement>

    @Insert
    fun insert(achievement: SingularAchievement): Long

    @Delete
    fun delete(achievement: SingularAchievement): Int
}