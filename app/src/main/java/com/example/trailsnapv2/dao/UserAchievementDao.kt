package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.UserAchievement

/**
 * Data Access Object (DAO) for the UserAchievement entity.
 * This interface provides methods to interact with the "user_achievements" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting singular achievement records.
 * It defines the necessary queries to manage user achievement-related data within the application.
 */
@Dao
interface UserAchievementDao {

    /**
     * Retrieves all user achievements from the `user_achievements` table.
     *
     * @return A list of all `UserAchievement` objects in the table.
     */
    @Query("SELECT * FROM user_achievements")
    fun getAll(): List<UserAchievement>

    /**
     * Retrieves all achievements associated with a specific user ID.
     *
     * @param userId The ID of the user whose achievements are to be retrieved.
     * @return A list of `UserAchievement` objects for the specified user.
     */
    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    fun getByUserId(userId: Long): List<UserAchievement>

    /**
     * Inserts a single user achievement into the `user_achievements` table.
     *
     * @param userAchievement The `UserAchievement` object to be inserted.
     * @return The ID of the newly inserted achievement.
     */
    @Insert
    fun insert(userAchievement: UserAchievement): Long

    /**
     * Deletes a specific user achievement from the `user_achievements` table.
     *
     * @param userAchievement The `UserAchievement` object to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    @Delete
    fun delete(userAchievement: UserAchievement): Int

    /**
     * Inserts multiple user achievements into the `user_achievements` table.
     *
     * @param userAchievements Vararg of `UserAchievement` objects to be inserted.
     * @return A list of IDs of the newly inserted achievements.
     */
    @Insert
    fun insertAll(vararg userAchievements: UserAchievement): List<Long>
}