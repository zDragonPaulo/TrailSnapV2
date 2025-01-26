package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Walk

@Dao
interface WalkDao {
    @Insert
    suspend fun insertWalk(walk: Walk): Long  // Insert Walk and return Long id

    @Query("SELECT * FROM walks WHERE walk_id = :id LIMIT 1")
    suspend fun getWalkById(id: Long): Walk?  // Get Walk by ID, returns Walk or null

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_id = :userId LIMIT 1)")
    suspend fun userExists(userId: Long): Boolean  // Check if user exists, returns Boolean

    @Query("SELECT * FROM walks WHERE user_id = :userId")
    suspend fun getWalksByUserId(userId: Long): List<Walk>

    // Function to calculate the total distance walked by a user
    @Query("SELECT SUM(distance) FROM walks WHERE user_id = :userId")
    suspend fun calculateDistanceWalked(userId: Long): Double

    // Function to calculate the number of walks completed by a user
    @Query("SELECT COUNT(*) FROM walks WHERE user_id = :userId")
    suspend fun calculateWalksCompleted(userId: Long): Int
}
