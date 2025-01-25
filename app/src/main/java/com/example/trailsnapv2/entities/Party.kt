package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a party in the TrailSnap application.
 * This entity corresponds to the "party" table in the database and stores information
 * about a group of users (a party) who participate in activities together. It includes
 * details about the party such as its name, description, creator, and statistics on the
 * total distance traveled and time spent.
 *
 * The party has a foreign key relationship with the User entity, linking the creator
 * of the party to a user who created it.
 *
 * @property party_id Unique identifier for the party. This is the primary key and is auto-generated.
 * @property party_name The name of the party.
 * @property party_description A brief description of the party.
 * @property creation_date The date when the party was created, stored as a string.
 * @property creator_id The ID of the user who created the party. This links to the User entity.
 * @property total_distance The total distance (in kilometers or miles) covered by the party.
 * @property time_used The total time (in milliseconds) the party has spent on activities.
 */
@Entity(
    tableName = "party",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["creator_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["creator_id"])]
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