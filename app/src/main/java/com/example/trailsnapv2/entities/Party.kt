package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "party",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["creator_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Party(
    @PrimaryKey(autoGenerate = true) val party_id: Long,
    val party_name: String,
    val party_description: String,
    val creation_date: String,
    val creator_id: Long,
    val total_distance: Double,
    val time_used: Long
)