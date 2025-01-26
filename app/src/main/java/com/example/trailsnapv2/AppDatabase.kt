package com.example.trailsnapv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trailsnapv2.dao.MemberDao
import com.example.trailsnapv2.dao.PartyAchievementDao
import com.example.trailsnapv2.dao.PartyDao
import com.example.trailsnapv2.dao.SingularAchievementDao
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Member
import com.example.trailsnapv2.entities.Party
import com.example.trailsnapv2.entities.PartyAchievement
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.User
import com.example.trailsnapv2.entities.Walk

/**
 * AppDatabase is the main database for the TrailSnap application.
 * It is built using Room and contains DAOs for interacting with different tables in the database.
 * This database manages entities such as users, walks, members, parties, and achievements.
 *
 * The database is designed to allow concurrent access and includes migration logic for schema updates.
 */
@Database(
    entities = [User::class, Walk::class, Member::class, Party::class, SingularAchievement::class, PartyAchievement::class],
    version = 2
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

        /**
         * Retrieves the singleton instance of the database, creating it if necessary.
         *
         * @param context The application context.
         * @return The AppDatabase instance.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        /**
         * Builds the Room database with the specified context and migration logic.
         *
         * @param context The application context.
         * @return A new instance of AppDatabase.
         */
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database.db"
            )
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build()

        val MIGRATION_1_2 = object : Migration(1, 2) {

            /**
             * Performs the migration from version 1 to version 2.
             *
             * @param database The SQLite database to be migrated.
             */
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