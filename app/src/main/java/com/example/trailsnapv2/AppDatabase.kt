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
    version = 11 // Increment the version number
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun walkDao(): WalkDao
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun singularAchievementDao(): SingularAchievementDao
    abstract fun partyAchievementDao(): PartyAchievementDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database.db"
            )
                .addMigrations(MIGRATION_10_11) // Add the new migration
                .allowMainThreadQueries() // for now :)
                .build()

        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migration logic from version 10 to 11
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `users_new` (
                        `user_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        `username` TEXT NOT NULL,
                        `password` TEXT NOT NULL,
                        `user_description` TEXT NOT NULL,
                        `birthday` TEXT NOT NULL,
                        `total_distance` REAL NOT NULL,
                        `time_used` INTEGER NOT NULL,
                        `creation_date` TEXT NOT NULL,
                        `profile_picture` TEXT
                    )
                """.trimIndent())

                database.execSQL("""
                    INSERT INTO `users_new` (`user_id`, `username`, `password`, `user_description`, `birthday`, `total_distance`, `time_used`, `creation_date`, `profile_picture`)
                    SELECT `user_id`, `username`, `password`, `user_description`, `birthday`, `total_distance`, `time_used`, `creation_date`, `profile_picture`
                    FROM `users`
                """.trimIndent())

                database.execSQL("DROP TABLE `users`")
                database.execSQL("ALTER TABLE `users_new` RENAME TO `users`")
            }
        }
    }
}