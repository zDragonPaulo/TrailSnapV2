package com.example.trailsnapv2

import android.app.Application

/**
 * Custom Application class for the TrailSnap application.
 * This class is used to initialize global objects such as the AppDatabase
 * that are required throughout the lifecycle of the app.
 */
class MyApp : Application() {

    /**
     * Lazy initialization of the AppDatabase instance.
     * The database is created only when it is first accessed.
     *
     * @return The AppDatabase instance used for database operations.
     */
    val database: AppDatabase by lazy { AppDatabase(this) }
}