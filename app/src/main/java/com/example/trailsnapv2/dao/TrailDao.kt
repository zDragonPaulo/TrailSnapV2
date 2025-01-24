package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Trail

@Dao
interface TrailDao {
    @Query("SELECT * FROM trails")
    fun getAll(): List<Trail>

    @Insert
    fun insert(trail: Trail): Long

    @Delete
    fun delete(trail: Trail): Int
}