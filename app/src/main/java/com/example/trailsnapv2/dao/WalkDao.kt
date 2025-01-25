package com.example.trailsnapv2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Walk

@Dao
interface WalkDao {
    @Query("SELECT * FROM walks WHERE walk_id = :walkId")
    fun getWalkById(walkId: Long): LiveData<Walk>

    @Insert
    fun insert(walk: Walk): Long

    @Query("SELECT COUNT(*) > 0 FROM users WHERE user_id = :userId")
    fun userExists(userId: Long): Boolean
}