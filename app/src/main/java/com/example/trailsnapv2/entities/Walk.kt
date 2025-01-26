package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "walks",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"])]
)
data class Walk(
    @PrimaryKey(autoGenerate = true) val walk_id: Long = 0,
    val user_id: Long,
    val walk_name: String,
    val distance: Double,
    val start_time: Long,
    val end_time: Long
)