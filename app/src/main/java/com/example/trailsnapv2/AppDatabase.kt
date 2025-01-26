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
import com.example.trailsnapv2.dao.UserAchievementDao
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.Member
import com.example.trailsnapv2.entities.Party
import com.example.trailsnapv2.entities.PartyAchievement
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.User
import com.example.trailsnapv2.entities.UserAchievement
import com.example.trailsnapv2.entities.Walk

@Database(
    entities = [User::class, Walk::class, Member::class, Party::class, UserAchievement::class, SingularAchievement::class, PartyAchievement::class],
    version = 15
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun walkDao(): WalkDao
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun singularAchievementDao(): SingularAchievementDao
    abstract fun userAchievementDao(): UserAchievementDao
    abstract fun partyAchievementDao(): PartyAchievementDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instanceForNow = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trailsnap_database"
                ).build()
                instance = instanceForNow
                instanceForNow
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database.db"
            )
                // Add migration handling here
                .addMigrations(MIGRATION_14_15)  // Add migration from version 14 to 15
                .fallbackToDestructiveMigration() // For non-migrated versions, Room will destroy the database and recreate it
                .build()
        }

        // Define the migration from version 14 to 15 (for example, change start_time and end_time from String to Long)
        val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create a temporary table with the new schema
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS walks_new (
                        walk_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        user_id INTEGER NOT NULL,
                        walk_name TEXT NOT NULL,
                        distance REAL NOT NULL,
                        start_time INTEGER NOT NULL,  -- Changed from String to Long
                        end_time INTEGER NOT NULL,    -- Changed from String to Long
                        FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
                    )
                """)

                // Copy data from old table to new table
                database.execSQL("""
                    INSERT INTO walks_new (walk_id, user_id, walk_name, distance, start_time, end_time)
                    SELECT walk_id, user_id, walk_name, distance, start_time, end_time FROM walks
                """)

                // Remove the old table
                database.execSQL("DROP TABLE walks")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE walks_new RENAME TO walks")
            }
        }
    }
}
