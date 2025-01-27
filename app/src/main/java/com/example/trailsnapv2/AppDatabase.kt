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
    entities = [
        User::class,
        Walk::class,
        Member::class,
        Party::class,
        UserAchievement::class,
        SingularAchievement::class,
        PartyAchievement::class
    ],
    version = 17 // Atualizei para a nova versão
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

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also { instance = it }
            }
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
                .addMigrations(MIGRATION_14_15, MIGRATION_15_16, MIGRATION_16_17) // Incluí todas as migrações
                .fallbackToDestructiveMigration()
                .build()
        }

        /**
         * Migração da versão 14 para 15: alteração de tipos de colunas.
         */
        private val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Cria uma nova tabela com o esquema atualizado
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

                // Copia os dados da tabela antiga para a nova
                database.execSQL("""
                    INSERT INTO walks_new (walk_id, user_id, walk_name, distance, start_time, end_time)
                    SELECT walk_id, user_id, walk_name, distance, start_time, end_time FROM walks
                """)

                // Remove a tabela antiga e renomeia a nova
                database.execSQL("DROP TABLE walks")
                database.execSQL("ALTER TABLE walks_new RENAME TO walks")
            }
        }

        /**
         * Migration from version 15 to 16: Adding `photo_path` column to `walks`.
         */
        private val MIGRATION_15_16 = object : Migration(15, 16) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adiciona a nova coluna `photo_path` à tabela existente
                database.execSQL("ALTER TABLE walks ADD COLUMN photo_path TEXT")
            }
        }

        /**
         * Migration from version 16 to 17: Adding `condition` column to `singular_achievements`.
         */
        private val MIGRATION_16_17 = object : Migration(16, 17) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the `condition` column with a default value
                database.execSQL("ALTER TABLE singular_achievements ADD COLUMN condition TEXT NOT NULL DEFAULT ''")
            }
        }

    }
}
