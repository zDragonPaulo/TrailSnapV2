package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "trails",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Trail(
    @PrimaryKey(autoGenerate = true) val trail_id: Long,
    val user_id: Long,
    val trail_name: String,
    val distance: Double,
    val start_time: String,
    val end_time: String
)