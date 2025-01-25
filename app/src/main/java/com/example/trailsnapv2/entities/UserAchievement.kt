package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_achievements",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SingularAchievement::class,
            parentColumns = ["id_achievement"],
            childColumns = ["achievement_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserAchievement(
    @PrimaryKey(autoGenerate = true) val id_user_achievement: Long,
    val user_id: Long,
    val achievement_id: Long,
    val progress: Double,
    val unlocked: Boolean
)