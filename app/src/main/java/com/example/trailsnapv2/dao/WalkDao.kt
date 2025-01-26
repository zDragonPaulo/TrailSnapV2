package com.example.trailsnapv2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Walk

/**
 * Data Access Object (DAO) for the Walk entity.
 * This interface provides methods to interact with the "walks" table in the database,
 * allowing operations such as retrieving walk details, inserting new walks, and verifying if a user exists.
 * It defines the necessary queries to manage walk-related data within the application.
 */
@Dao
interface WalkDao {

    /**
     * Retrieves a walk by its ID from the "walks" table.
     *
     * This method fetches a single walk record based on the provided `walkId`. The result is wrapped in a `LiveData`,
     * allowing the caller to observe changes to the walk's data in real-time.
     *
     * @param walkId The unique identifier of the walk.
     * @return A `LiveData` object that emits the `Walk` object associated with the given `walkId`, or `null` if no such walk exists.
     */
    @Query("SELECT * FROM walks WHERE walk_id = :walkId")
    fun getWalkById(walkId: Long): LiveData<Walk>

    /**
     * Inserts a new walk into the "walks" table.
     *
     * This method inserts a new `Walk` record into the database. It returns the row ID of the newly inserted walk.
     *
     * @param walk The `Walk` object to be inserted into the database.
     * @return The row ID of the newly inserted walk record.
     */
    @Insert
    fun insert(walk: Walk): Long

    /**
     * Checks if a user exists in the database based on the provided `userId`.
     *
     * This method verifies if the user with the given `userId` exists in the `users` table. The result is a boolean value.
     * If the user exists, the method returns `true`; otherwise, it returns `false`.
     *
     * @param userId The unique identifier of the user to check.
     * @return A boolean value indicating whether the user exists in the database.
     */
    @Query("SELECT COUNT(*) > 0 FROM users WHERE user_id = :userId")
    fun userExists(userId: Long): Boolean
}
