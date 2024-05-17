package com.hong7.coinnews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FilterEntity::class, ScrapNewsEntity::class, NewsEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun filterDao(): UserFilterDao

    abstract fun interestedNewsDao(): InterestedNewsDao

    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        """
                            CREATE TABLE IF NOT EXISTS `scrap_news` (
                                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                `news_id` TEXT NOT NULL,
                                `title` TEXT NOT NULL,
                                `description` TEXT NOT NULL,
                                `url` TEXT NOT NULL,
                                `author` TEXT,
                                `put_date` INTEGER NOT NULL
                            )
                        """.trimIndent()
                    )
                }
            }

            return Room.databaseBuilder(context, AppDatabase::class.java, "coin_db")
                .addMigrations(MIGRATION_1_2)
                .addTypeConverter(Converter())
                .build()
        }
    }
}
