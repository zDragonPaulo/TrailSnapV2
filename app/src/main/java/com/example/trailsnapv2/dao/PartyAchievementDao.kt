package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.PartyAchievement

@Dao
interface PartyAchievementDao {
    @Query("SELECT * FROM party_achievements")
    fun getAll(): List<PartyAchievement>

    @Insert
    fun insert(achievement: PartyAchievement): Long

    @Delete
    fun delete(achievement: PartyAchievement): Int
}