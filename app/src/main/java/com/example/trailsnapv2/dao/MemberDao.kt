package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.Member

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAll(): List<Member>

    @Insert
    fun insert(member: Member): Long

    @Delete
    fun delete(member: Member): Int
}