package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trailsnapv2.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?

    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User): Int
}