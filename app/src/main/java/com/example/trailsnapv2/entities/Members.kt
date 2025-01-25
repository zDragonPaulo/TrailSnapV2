package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a member of a party in the TrailSnap application.
 * This entity corresponds to the "members" table in the database and stores information
 * about users who are part of a party. Each member is associated with both a user and a party.
 * It includes details about the user who joined the party, the party they belong to, and
 * the date they joined the party.
 *
 * This entity has foreign key relationships with both the User and Party entities.
 * - `user_id` links to the User who is the member of the party.
 * - `party_id` links to the Party to which the user belongs.
 *
 * @property member_id Unique identifier for the member. This is the primary key and is auto-generated.
 * @property user_id The ID of the user who is part of the party. This links to the User entity.
 * @property party_id The ID of the party that the user belongs to. This links to the Party entity.
 * @property join_date The date when the user joined the party, stored as a string.
 */
@Entity(
    tableName = "members",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Party::class,
            parentColumns = ["party_id"],
            childColumns = ["party_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Member(
    @PrimaryKey(autoGenerate = true) val member_id: Long,
    val user_id: Long,
    val party_id: Long,
    val join_date: String
)
