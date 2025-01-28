package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.SingularAchievement

/**
 * Data Access Object (DAO) for the SingularAchievement entity.
 * This interface provides methods to interact with the "SingularAchievement" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting singular achievement records.
 * It defines the necessary queries to manage Singular Achievement-related data within the application.
 */
@Dao
interface SingularAchievementDao {

    /**
     * Inserts a single SingularAchievement into the database.
     *
     * @param achievement The SingularAchievement to insert.
     * @throws Exception If there is an issue with the database insertion.
     */
    @Insert
    suspend fun insert(achievement: SingularAchievement)

    /**
     * Inserts a list of SingularAchievements into the database.
     *
     * @param achievements The list of SingularAchievements to insert.
     * @throws Exception If there is an issue with the database insertion.
     */
    @Insert
    suspend fun insertAll(achievements: List<SingularAchievement>)

    /**
     * Retrieves all SingularAchievements from the database.
     *
     * @return A list of all SingularAchievements stored in the database.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM singular_achievements")
    suspend fun getAll(): List<SingularAchievement>

    /**
     * Retrieves a SingularAchievement by its unique ID.
     *
     * @param achievementId The ID of the achievement to retrieve.
     * @return The SingularAchievement object with the specified ID, or null if not found.
     * @throws Exception If there is an issue with the database query.
     */
    @Query("SELECT * FROM singular_achievements WHERE id_achievement = :achievementId")
    suspend fun getById(achievementId: Long): SingularAchievement?

    /**
     * Deletes a SingularAchievement from the database by its unique ID.
     *
     * @param achievementId The ID of the achievement to delete.
     * @throws Exception If there is an issue with the database deletion.
     */
    @Query("DELETE FROM singular_achievements WHERE id_achievement = :achievementId")
    suspend fun deleteById(achievementId: Long)

    /**
     * Clears all SingularAchievements from the database.
     *
     * @throws Exception If there is an issue with clearing the database table.
     */
    @Query("DELETE FROM singular_achievements")
    suspend fun clearAll()
}
