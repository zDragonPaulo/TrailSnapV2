package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.SingularAchievement

/**
 * Data Access Object (DAO) for the SingularAchievement entity.
 * This interface provides methods to interact with the "singular_achievements" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting singular achievement records.
 * It defines the necessary queries to manage singular achievement-related data within the application.
 */
@Dao
interface SingularAchievementDao {

    /**
     * Retrieves all singular achievements from the "singular_achievements" table.
     *
     * @return A list of all singular achievements in the database.
     */
    @Query("SELECT * FROM singular_achievements")
    fun getAll(): List<SingularAchievement>

    /**
     * Inserts a new singular achievement into the "singular_achievements" table.
     *
     * @param achievement The SingularAchievement object to be inserted.
     * @return The row ID of the newly inserted achievement.
     */
    @Insert
    fun insert(achievement: SingularAchievement): Long

    /**
     * Deletes the specified singular achievement from the "singular_achievements" table.
     *
     * @param achievement The SingularAchievement object to be deleted.
     * @return The number of rows affected (1 if successful, 0 if the achievement doesn't exist).
     */
    @Delete
    fun delete(achievement: SingularAchievement): Int

    /**
     * Deletes all singular achievements from the "singular_achievements" table.
     *
     * This method removes all records from the `singular_achievements` table. This operation is irreversible,
     * so use it with caution. It doesn't take any parameters and clears the entire table of singular achievements.
     *
     * @return The number of rows affected (which will be the number of rows deleted from the table).
     */
    @Query("DELETE FROM singular_achievements")
    fun clearAll()
}
