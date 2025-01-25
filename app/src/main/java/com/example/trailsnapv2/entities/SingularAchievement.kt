package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "singular_achievements",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["receiver_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["receiver_id"])]
)
data class SingularAchievement(
    @PrimaryKey(autoGenerate = true) val id_achievement: Long,
    val receiver_id: Long,
    val name_achievement: String,
    val description_achievement: String,
    val unlocked: Boolean,
    val progress: Double
)