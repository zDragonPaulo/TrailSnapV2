package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val user_id: Long,
    val password: String,
    val username: String,
    val user_description: String,
    val birthday: String,
    val total_distance: Double,
    val time_used: Long,
    val creation_date: String,
    val profile_picture: String
)