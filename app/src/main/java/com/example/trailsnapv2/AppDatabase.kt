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

/**
 * Singleton Room Database for the TrailSnap application.
 *
 * This database contains tables for users, walks, members, parties, achievements, and related entities.
 * It uses Room's database builder to ensure thread safety and supports migrations to handle schema updates.
 *
 * @property userDao Provides access to the User entity.
 * @property walkDao Provides access to the Walk entity.
 * @property memberDao Provides access to the Member entity.
 * @property partyDao Provides access to the Party entity.
 * @property singularAchievementDao Provides access to Singular Achievements.
 * @property userAchievementDao Provides access to User Achievements.
 * @property partyAchievementDao Provides access to Party Achievements.
 */
@Database(
    entities = [
        User::class,
        Walk::class,
        Member::class,
        Party::class,
        UserAchievement::class,
        SingularAchievement::class,
        PartyAchievement::class
    ],
    version = 17
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

        /**
         * Creates or retrieves the singleton instance of the database.
         *
         * @param context The application context.
         * @return The singleton instance of the database.
         */
        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /**
         * Retrieves the database instance, initializing it if necessary.
         *
         * @param context The application context.
         * @return The database instance.
         */
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

        /**
         * Builds the database with support for migrations.
         *
         * @param context The application context.
         * @return A fully configured Room database.
         */
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database.db"
            )
                .addMigrations(MIGRATION_14_15, MIGRATION_15_16, MIGRATION_16_17)
                .fallbackToDestructiveMigration()
                .build()
        }

        private val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS walks_new (
                        walk_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        user_id INTEGER NOT NULL,
                        walk_name TEXT NOT NULL,
                        distance REAL NOT NULL,
                        start_time INTEGER NOT NULL, -- Alterado para Long
                        end_time INTEGER NOT NULL,   -- Alterado para Long
                        FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
                    )
                """)

                database.execSQL("""
                    INSERT INTO walks_new (walk_id, user_id, walk_name, distance, start_time, end_time)
                    SELECT walk_id, user_id, walk_name, distance, start_time, end_time FROM walks
                """)

                database.execSQL("DROP TABLE walks")
                database.execSQL("ALTER TABLE walks_new RENAME TO walks")
            }
        }

        private val MIGRATION_15_16 = object : Migration(15, 16) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE walks ADD COLUMN photo_path TEXT")
            }
        }

        private val MIGRATION_16_17 = object : Migration(16, 17) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE singular_achievements ADD COLUMN condition TEXT NOT NULL DEFAULT ''")
            }
        }

    }
}
