package com.example.trailsnapv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trailsnapv2.dao.*
import com.example.trailsnapv2.entities.*


/**
 * Represents the Room database for the TrailSnap application.
 * This database includes various entities such as User, Trail, Member, Party,
 * SingularAchievement, and PartyAchievement. It provides abstract methods to access
 * their respective DAO (Data Access Object) interfaces.
 *
 * @property userDao DAO for accessing user-related data.
 * @property trailDao DAO for accessing trail-related data.
 * @property memberDao DAO for accessing member-related data.
 * @property partyDao DAO for accessing party-related data.
 * @property singularAchievementDao DAO for accessing singular achievement data.
 * @property partyAchievementDao DAO for accessing party achievement data.
 */
@Database(
    entities = [User::class, Trail::class, Member::class, Party::class, SingularAchievement::class,
        PartyAchievement::class, UserAchievement::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trailDao(): TrailDao
    abstract fun memberDao(): MemberDao
    abstract fun partyDao(): PartyDao
    abstract fun singularAchievementDao(): SingularAchievementDao
    abstract fun partyAchievementDao(): PartyAchievementDao
    abstract fun userAchievementDao(): UserAchievementDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        /**
         * Returns a singleton instance of the AppDatabase. If the instance is not created yet,
         * it initializes the database.
         *
         * @param context The application context used to build the database.
         * @return The singleton instance of the AppDatabase.
         */
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        /**
         * Builds the Room database instance with necessary configurations such as migrations.
         *
         * @param context The application context used to build the database.
         * @return The built Room database instance.
         */
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database.db")
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build()

        /**
         * Migration from version 1 to version 2 of the database.
         * This migration adds a new column 'profile_picture' to the 'users' table.
         */
        val MIGRATION_1_2 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN profile_picture TEXT NOT NULL DEFAULT '@drawable/ic_user_placeholder.png'")
            }
        }
    }
}