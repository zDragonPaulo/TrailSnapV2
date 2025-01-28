package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.Walk

/**
 * Data Access Object (DAO) for the Walk entity.
 * This interface provides methods to interact with the "Walk" table in the database,
 * allowing for operations such as retrieving, inserting, and calculating walk records.
 * It defines the necessary queries to manage Walk-related data within the application.
 */
@Dao
interface WalkDao {

    /**
     * Inserts a new Walk into the database.
     *
     * @param walk The Walk object to be inserted.
     * @return The ID of the inserted Walk (Long).
     * @throws Exception If there is an issue with the database insertion.
     */
    @Insert
    suspend fun insertWalk(walk: Walk): Long  // Insert Walk and return Long id

    /**
     * Retrieves a Walk by its unique ID.
     *
     * @param id The ID of the Walk to retrieve.
     * @return The Walk object with the specified ID, or null if not found.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM walks WHERE walk_id = :id LIMIT 1")
    suspend fun getWalkById(id: Long): Walk?  // Get Walk by ID, returns Walk or null

    /**
     * Checks if a user exists in the database.
     *
     * @param userId The ID of the user to check.
     * @return True if the user exists, false otherwise.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_id = :userId LIMIT 1)")
    suspend fun userExists(userId: Long): Boolean  // Check if user exists, returns Boolean

    /**
     * Retrieves all Walks for a specific user, ordered by Walk ID in descending order.
     *
     * @param userId The ID of the user whose walks are to be retrieved.
     * @return A list of Walks for the specified user.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM walks WHERE user_id = :userId ORDER BY walk_id DESC")
    suspend fun getWalksByUserId(userId: Long): List<Walk>

    /**
     * Calculates the total distance walked by a user.
     *
     * @param userId The ID of the user for whom to calculate the total distance.
     * @return The total distance walked by the user (Double).
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT SUM(distance) FROM walks WHERE user_id = :userId")
    suspend fun calculateDistanceWalked(userId: Long): Double

    /**
     * Calculates the number of walks completed by a user.
     *
     * @param userId The ID of the user for whom to calculate the number of walks.
     * @return The number of walks completed by the user (Int).
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT COUNT(*) FROM walks WHERE user_id = :userId")
    suspend fun calculateWalksCompleted(userId: Long): Int

    /**
     * Updates an existing Walk in the database.
     *
     * @param walk The Walk object with updated information to be saved in the database.
     * @return The number of rows affected by the update (Int).
     * @throws Exception If there is an issue with the database update.
     */
    @Update
    suspend fun updateWalk(walk: Walk): Int
}
