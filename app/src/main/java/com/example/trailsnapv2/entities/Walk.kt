package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a walk activity within the TrailSnap application.
 * This entity corresponds to the "walks" table in the database and stores information
 * about individual walks taken by users, including details such as the name, distance,
 * and the start and end times of the walk.
 *
 * The entity establishes a foreign key relationship with the User entity, linking each walk
 * to the user who completed it. When a user is deleted, all their associated walks are also
 * deleted due to the cascading delete behavior.
 *
 * @property walk_id Unique identifier for the walk. This is the primary key and is auto-generated.
 * @property user_id The unique identifier of the user who completed the walk. This is a foreign key to the User entity.
 * @property walk_name The name or title of the walk.
 * @property distance The total distance covered during the walk, in kilometers or other units.
 * @property start_time The start time of the walk, stored as a string (e.g., timestamp or formatted date).
 * @property end_time The end time of the walk, stored as a string (e.g., timestamp or formatted date).
 */
@Entity(
    tableName = "walks",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]

)

data class Walk(
    @PrimaryKey(autoGenerate = true) val walk_id: Long,
    val user_id: Long,
    val walk_name: String,
    val distance: Double,
    val start_time: String,
    val end_time: String
)