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
    entities = [User::class, Walk::class, Member::class, Party::class, SingularAchievement::class, PartyAchievement::class],
    version = 10 // Increment the version number
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun walkDao(): WalkDao
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun singularAchievementDao(): SingularAchievementDao
    abstract fun partyAchievementDao(): PartyAchievementDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database.db")
                .addMigrations(MIGRATION_7_8, MIGRATION_8_9)
                .allowMainThreadQueries() // for now :)
                .build()

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 7 to 8
                // Example: database.execSQL("ALTER TABLE users ADD COLUMN new_column TEXT")
            }
        }

        private val MIGRATION_8_9 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 8 to 9
                // Example: database.execSQL("ALTER TABLE users ADD COLUMN another_new_column INTEGER")
            }
        }
    }
}