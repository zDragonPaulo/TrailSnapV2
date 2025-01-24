package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    fun isUnlocked(): Boolean {
        return unlocked
    }
}