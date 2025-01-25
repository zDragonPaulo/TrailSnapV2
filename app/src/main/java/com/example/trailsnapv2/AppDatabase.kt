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
    version = 2 // Increment the version number
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
                .addMigrations(MIGRATION_1_2) // Add the new migration
                .allowMainThreadQueries() // for now :)
                .build()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `walks` (
                `walk_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `walk_name` TEXT NOT NULL,
                `start_time` TEXT NOT NULL,
                `end_time` TEXT NOT NULL,
                `distance` REAL NOT NULL,
                `user_id` INTEGER NOT NULL,
                FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE
            )
        """)
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_walks_user_id` ON `walks`(`user_id`)")
            }
        }

    }
}