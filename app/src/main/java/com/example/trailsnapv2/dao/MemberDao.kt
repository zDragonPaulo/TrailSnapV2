package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Member

/**
 * Data Access Object (DAO) for the Member entity.
 * This interface provides methods to interact with the "members" table in the database,
 * allowing for operations such as retrieving, inserting, and deleting member records.
 * It defines the necessary queries to manage member-related data within the application.
 */
@Dao
interface MemberDao {

    /**
     * Retrieves all members from the "members" table.
     *
     * @return A list of all members in the database.
     */
    @Query("SELECT * FROM members")
    fun getAll(): List<Member>

    /**
     * Inserts a new member into the "members" table.
     *
     * @param member The Member object to be inserted.
     * @return The row ID of the newly inserted member.
     */
    @Insert
    fun insert(member: Member): Long

    /**
     * Deletes the specified member from the "members" table.
     *
     * @param member The Member object to be deleted.
     * @return The number of rows affected (1 if successful, 0 if the member doesn't exist).
     */
    @Delete
    fun delete(member: Member): Int
}
