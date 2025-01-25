package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a singular achievement in the TrailSnap application.
 * This entity corresponds to the "singular_achievements" table in the database and
 * stores information about individual achievements a user can unlock or track progress for.
 * It also contains a foreign key relationship with the User entity, linking the achievement
 * to the user who earned it.
 *
 * @property id_achievement Unique identifier for the achievement. This is the primary key and is auto-generated.
 * @property receiver_id The ID of the user who unlocked or is associated with this achievement.
 * @property name_achievement The name of the achievement.
 * @property description_achievement A brief description of what the achievement entails.
 * @property unlocked A boolean indicating whether the achievement has been unlocked or not.
 * @property progress The current progress towards unlocking the achievement, represented as a percentage.
 */
@Entity(
    tableName = "singular_achievements"
)
data class SingularAchievement(
    @PrimaryKey(autoGenerate = true) val id_achievement: Long,
    val name_achievement: String,
    val description_achievement: String,
)