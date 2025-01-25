package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Represents a party achievement in the TrailSnap application.
 * This entity corresponds to the "party_achievements" table in the database and
 * stores information about achievements that are unlocked or tracked by a party of users.
 * It also contains a foreign key relationship with the Party entity, linking the achievement
 * to the party that earned it.
 *
 * @property id_achievement Unique identifier for the achievement. This is the primary key and is auto-generated.
 * @property party_id The ID of the party that unlocked or is associated with this achievement.
 * @property name_achievement The name of the achievement.
 * @property description_achievement A brief description of what the achievement entails.
 * @property unlocked A boolean indicating whether the achievement has been unlocked or not.
 * @property progress The current progress towards unlocking the achievement, represented as a percentage.
 */
@Entity(
    tableName = "party_achievements",
    foreignKeys = [ForeignKey(
        entity = Party::class,
        parentColumns = ["party_id"],
        childColumns = ["party_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PartyAchievement(
    @PrimaryKey(autoGenerate = true) val id_achievement: Long,
    val party_id: Long,
    val name_achievement: String,
    val description_achievement: String,
    val unlocked: Boolean,
    val progress: Double
) {
    @Ignore
    fun isUnlocked(): Boolean {
        return unlocked
    }
}