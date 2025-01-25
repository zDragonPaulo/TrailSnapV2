package com.example.trailsnapv2

import android.app.Application

class MyApp : Application() {
    val database: AppDatabase by lazy { AppDatabase(this) }
}