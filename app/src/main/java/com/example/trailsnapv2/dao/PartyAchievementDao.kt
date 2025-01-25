package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.PartyAchievement

/**
 * Data Access Object (DAO) for the PartyAchievement entity.
 * This interface provides methods to interact with the "party_achievements" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting party achievement records.
 * It defines the necessary queries to manage party achievement-related data within the application.
 */
@Dao
interface PartyAchievementDao {

    /**
     * Retrieves all party achievements from the "party_achievements" table.
     *
     * @return A list of all party achievements in the database.
     */
    @Query("SELECT * FROM party_achievements")
    fun getAll(): List<PartyAchievement>

    /**
     * Inserts a new party achievement into the "party_achievements" table.
     *
     * @param achievement The PartyAchievement object to be inserted.
     * @return The row ID of the newly inserted party achievement.
     */
    @Insert
    fun insert(achievement: PartyAchievement): Long

    /**
     * Deletes the specified party achievement from the "party_achievements" table.
     *
     * @param achievement The PartyAchievement object to be deleted.
     * @return The number of rows affected (1 if successful, 0 if the achievement doesn't exist).
     */
    @Delete
    fun delete(achievement: PartyAchievement): Int
}
