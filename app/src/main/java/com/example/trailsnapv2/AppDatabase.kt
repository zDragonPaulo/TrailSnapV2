package com.example.trailsnapv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trailsnapv2.dao.*
import com.example.trailsnapv2.entities.*

@Database(
    entities = [
        User::class,
        Trail::class,
        Member::class,
        Party::class,
        SingularAchievement::class,
        PartyAchievement::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trailDao(): TrailDao
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun singularAchievementDao(): SingularAchievementDao
    abstract fun partyAchievementDao(): PartyAchievementDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database.db")
                .allowMainThreadQueries()
                .build()
    }
}