package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a user achievement within the TrailSnap application.
 * This entity corresponds to the "user_achievements" table in the database and tracks
 * the achievements earned by users, as well as their progress towards those achievements.
 * It establishes a many-to-one relationship with both the User and SingularAchievement entities,
 * linking each user to the achievements they have earned or are progressing towards.
 *
 * The entity contains foreign key relationships with the User and SingularAchievement entities.
 * When a user or achievement is deleted, the associated records in the "user_achievements" table
 * will also be removed due to the cascading delete behavior.
 *
 * @property id_user_achievement Unique identifier for the user achievement. This is the primary key and is auto-generated.
 * @property user_id The unique identifier of the user who earned or is progressing towards the achievement. This is a foreign key to the User entity.
 * @property achievement_id The unique identifier of the achievement that the user has earned or is tracking progress for. This is a foreign key to the SingularAchievement entity.
 * @property progress The progress made by the user towards unlocking the achievement, represented as a percentage or numerical value.
 * @property unlocked A flag indicating whether the achievement has been unlocked by the user.
 */
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
    ],
    indices = [
        Index(value = ["user_id"]), // Index for user_id
        Index(value = ["achievement_id"]) // Index for achievement_id
    ]
)
data class UserAchievement(
    @PrimaryKey(autoGenerate = true) val id_user_achievement: Long,
    val user_id: Long,
    val achievement_id: Long,
    val progress: Double,
    val unlocked: Boolean
)
