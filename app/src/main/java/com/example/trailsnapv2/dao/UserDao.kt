package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the User entity.
 * This interface provides methods to interact with the "users" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting user records.
 * It defines the necessary queries to manage user-related data within the application.
 */
@Dao
interface UserDao {

    /**
     * Retrieves all users from the "users" table.
     *
     * @return A list of all users in the database.
     */
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    /**
     * Retrieves a user from the "users" table based on the provided username.
     *
     * @param username The username of the user to be retrieved.
     * @return A User object if found, or null if no user with the given username exists.
     */
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?

    /**
     * Inserts a new user into the "users" table.
     *
     * @param user The User object to be inserted.
     * @return The row ID of the newly inserted user.
     */
    @Insert
    fun insert(user: User): Long

    /**
     * Deletes the specified user from the "users" table.
     *
     * @param user The User object to be deleted.
     * @return The number of rows affected (1 if successful, 0 if the user doesn't exist).
     */
    @Delete
    fun delete(user: User): Int

    /**
     * Retrieves a user from the "users" table based on the provided user ID, and returns
     * a Flow object that can be observed for updates.
     *
     * @param userId The ID of the user to be retrieved.
     * @return A Flow emitting the User object, or null if no user with the given ID exists.
     */
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Long): Flow<User?>
}
