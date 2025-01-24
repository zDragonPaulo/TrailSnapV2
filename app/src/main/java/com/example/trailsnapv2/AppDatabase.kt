package com.example.trailsnapv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trailsnapv2.dao.*
import com.example.trailsnapv2.entities.*

@Database(
    entities = [User::class, Trail::class, Member::class, Party::class, SingularAchievement::class, PartyAchievement::class],
    version = 2 // Ensure the version is 2
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
                .addMigrations(MIGRATION_1_2)
                .build()

        // Migration definition
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN profile_picture TEXT NOT NULL DEFAULT 'default_profile_picture_path'")
            }
        }
    }
}