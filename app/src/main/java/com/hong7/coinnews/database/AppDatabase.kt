package com.hong7.coinnews.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.database.migration.AutoMigrationSpecs
import com.hong7.coinnews.database.migration.Migration.MIGRATION_1_2

@Database(
    entities = [CoinEntity::class],
    version = 5,
    autoMigrations = [
        AutoMigration(
            from = 2,
            to = 3,
            spec = AutoMigrationSpecs.MIGRATION_2_3_SPEC::class
        ),
        AutoMigration(
            from = 3,
            to = 4,
            spec = AutoMigrationSpecs.MIGRATION_3_4_SPEC::class
        ),
        AutoMigration(
            from = 4,
            to = 5,
            spec = AutoMigrationSpecs.MIGRATION_4_5_SPEC::class
        )
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "coin_db")
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
