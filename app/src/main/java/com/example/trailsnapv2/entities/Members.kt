package com.example.trailsnapv2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true) val id: Long,
    val user_id: Long,
    val party_id: Long,
    val join_date: String
)
