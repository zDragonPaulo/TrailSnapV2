package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement

/**
 * Data Access Object (DAO) for the UserAchievement entity.
 * This interface provides methods to interact with the "UserAchievement" table in the database,
 * allowing for operations such as retrieving, inserting, and updating user achievement records.
 * It defines the necessary queries to manage User Achievement-related data within the application.
 */
@Dao
interface UserAchievementDao {

    /**
     * Inserts a new UserAchievement into the database.
     *
     * @param userAchievement The UserAchievement object to be inserted.
     * @throws Exception If there is an issue with the database insertion.
     */
    @Insert
    suspend fun insert(userAchievement: UserAchievement)

    /**
     * Inserts a list of UserAchievements into the database.
     *
     * @param userAchievements The list of UserAchievement objects to be inserted.
     * @throws Exception If there is an issue with the database insertion.
     */
    @Insert
    suspend fun insertAll(userAchievements: List<UserAchievement>)

    /**
     * Retrieves all UserAchievements for a specific user by their user ID.
     *
     * @param userId The ID of the user whose achievements are to be retrieved.
     * @return A list of UserAchievement objects for the specified user.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM user_achievements WHERE user_id = :userId")
    suspend fun getUserAchievements(userId: Long): List<UserAchievement>

    /**
     * Updates a list of UserAchievements in the database.
     *
     * @param userAchievements The list of UserAchievement objects with updated information.
     * @throws Exception If there is an issue with the database update.
     */
    @Update
    suspend fun updateAll(userAchievements: List<UserAchievement>)

    /**
     * Retrieves the top 3 UserAchievements for a specific user that have a progress less than 100,
     * ordered by progress in descending order.
     *
     * @param userId The ID of the user whose top progress achievements are to be retrieved.
     * @return A list of the top 3 UserAchievement objects for the specified user.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM user_achievements WHERE user_id = :userId AND progress < 100 ORDER BY progress DESC LIMIT 3")
    suspend fun getTopProgressAchievements(userId: Long): List<UserAchievement>

    /**
     * Retrieves all SingularAchievements for a specific user based on their completed achievements.
     *
     * @param userId The ID of the user whose SingularAchievements are to be retrieved.
     * @return A list of SingularAchievement objects for the specified user.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM singular_achievements WHERE id_achievement IN (SELECT achievement_id FROM user_achievements WHERE user_id = :userId)")
    suspend fun getSingularAchievementsForUser(userId: Long): List<SingularAchievement>
}
