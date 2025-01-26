package com.example.trailsnapv2.entities

data class AchievementCondition(
    val metric: String,  // E.g., "distance_walked", "walks_completed"
    val target: Double   // E.g., 5.0 (for distance)
)

