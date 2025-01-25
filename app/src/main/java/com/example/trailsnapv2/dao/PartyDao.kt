package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Party

/**
 * Data Access Object (DAO) for the Party entity.
 * This interface provides methods to interact with the "party" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting party records.
 * It defines the necessary queries to manage party-related data within the application.
 */
@Dao
interface PartyDao {

    /**
     * Retrieves all parties from the "party" table.
     *
     * @return A list of all parties in the database.
     */
    @Query("SELECT * FROM party")
    fun getAll(): List<Party>

    /**
     * Inserts a new party into the "party" table.
     *
     * @param party The Party object to be inserted.
     * @return The row ID of the newly inserted party.
     */
    @Insert
    fun insert(party: Party): Long

    /**
     * Deletes the specified party from the "party" table.
     *
     * @param party The Party object to be deleted.
     * @return The number of rows affected (1 if successful, 0 if the party doesn't exist).
     */
    @Delete
    fun delete(party: Party): Int
}
