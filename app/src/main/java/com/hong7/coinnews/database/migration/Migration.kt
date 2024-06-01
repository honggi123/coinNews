package com.hong7.coinnews.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {

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
}