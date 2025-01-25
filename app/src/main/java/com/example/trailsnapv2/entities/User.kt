package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user in the TrailSnap application.
 * This entity corresponds to the "users" table in the database and contains all
 * the details related to a user profile such as personal information, activity data,
 * and profile picture.
 *
 * @property user_id Unique identifier for the user. This is the primary key and is auto-generated.
 * @property password The user's password, stored as a string.
 * @property username The username chosen by the user.
 * @property user_description A brief description of the user, provided by them.
 * @property birthday The user's birth date, stored as a string in the format "yyyy-MM-dd".
 * @property total_distance The total distance traveled by the user (in kilometers or miles).
 * @property time_used The total time (in milliseconds) the user has spent on walks or activities.
 * @property creation_date The date when the user profile was created, stored as a string.
 * @property profile_picture The URL or resource path to the user's profile picture. Defaults to a placeholder image.
 */
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
    val profile_picture: String?
)