package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Walk

@Dao
interface WalkDao {
    @Query("SELECT * FROM walks")
    fun getAll(): List<Walk>

    @Insert
    fun insert(walk: Walk): Long
}