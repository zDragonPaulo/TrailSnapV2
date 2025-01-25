package com.example.trailsnapv2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trailsnapv2.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String): User?

    @Query("SELECT MAX(user_id) FROM users")
    fun getMaxUserId(): Long?

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Long): Flow<User?>

    @Insert
    fun insert(user: User): Long

    @Update
    fun updateUser(user: User): Int
}