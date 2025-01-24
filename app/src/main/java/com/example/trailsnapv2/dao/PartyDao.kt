package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Party

@Dao
interface PartyDao {
    @Query("SELECT * FROM party")
    fun getAll(): List<Party>

    @Insert
    fun insert(party: Party): Long

    @Delete
    fun delete(party: Party): Int
}