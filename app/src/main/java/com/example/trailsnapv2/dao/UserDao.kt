package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the User entity.
 * This interface provides methods to interact with the "users" table in the database,
 * allowing for operations such as retrieving, inserting, and updating user records.
 * It defines the necessary queries to manage user-related data within the application.
 */
@Dao
interface UserDao {

    /**
     * Retrieves all users from the "users" table.
     *
     * This method fetches every record stored in the `users` table, returning them as a list of `User` objects.
     * It is useful when you need to access all users in the database.
     *
     * @return A list of all `User` objects in the database.
     */
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    /**
     * Retrieves a user by their username from the "users" table.
     *
     * This method fetches a single user record by the given `username`. If the user exists in the database,
     * it returns the matching `User` object. Otherwise, it returns `null`.
     *
     * @param username The username of the user to retrieve.
     * @return The `User` object associated with the specified username, or `null` if no user is found.
     */
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?

    /**
     * Retrieves the maximum user ID from the "users" table.
     *
     * This method retrieves the largest `user_id` from the `users` table. It is useful for determining the
     * highest value for user IDs, which can be useful for generating new user IDs or other related logic.
     *
     * @return The maximum `user_id` in the database, or `null` if no users exist.
     */
    @Query("SELECT MAX(user_id) FROM users")
    fun getMaxUserId(): Long?

    /**
     * Retrieves a user by their user ID from the "users" table.
     *
     * This method fetches a user record by the given `user_id`. The result is returned as a `Flow`, allowing
     * for reactive updates if the user data changes. It is useful for observing changes to user data in real-time.
     *
     * @param userId The user ID of the user to retrieve.
     * @return A `Flow` that emits the `User` object associated with the specified user ID, or `null` if no user is found.
     */
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Long): Flow<User?>

    /**
     * Inserts a new user into the "users" table.
     *
     * This method inserts a new `User` record into the database. It returns the row ID of the newly inserted user.
     * The insertion operation is performed on the background thread.
     *
     * @param user The `User` object to be inserted into the database.
     * @return The row ID of the newly inserted user.
     */
    @Insert
    fun insert(user: User): Long

    /**
     * Updates an existing user in the "users" table with new data.
     *
     * This method updates an existing user record in the database with new information. The user must already
     * exist in the database, identified by the primary key `user_id`. The method returns the number of rows
     * affected by the update operation (typically 1 if successful).
     *
     * @param user The `User` object containing the updated information.
     * @return The number of rows affected by the update operation.
     *         Typically, this should be 1 if the update is successful, or 0 if no matching record exists.
     */
    @Update
    fun updateUser(user: User): Int
}
