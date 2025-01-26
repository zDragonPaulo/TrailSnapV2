package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a member within the TrailSnap application.
 * This entity corresponds to the "members" table in the database and links a user to a party.
 * It establishes a many-to-many relationship between users and parties by associating
 * a user with a party they are a part of.
 *
 * The entity also contains foreign key relationships with the User and Party entities,
 * ensuring referential integrity between the tables. When a user or party is deleted,
 * all associated members are also removed due to the cascading delete behavior.
 *
 * @property member_id Unique identifier for the member. This is the primary key and is auto-generated.
 * @property user_id The unique identifier of the user associated with this member. This is a foreign key to the User entity.
 * @property party_id The unique identifier of the party this member belongs to. This is a foreign key to the Party entity.
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
    ],
    indices = [Index(value = ["user_id"]), Index(value = ["party_id"])]
)
data class Member(
    @PrimaryKey(autoGenerate = true) val member_id: Long,
    val user_id: Long,
    val party_id: Long
)