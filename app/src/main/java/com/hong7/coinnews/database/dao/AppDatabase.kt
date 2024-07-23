package com.hong7.coinnews.database.dao

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hong7.coinnews.database.Converter
import com.hong7.coinnews.database.InterestedNewsDao
import com.hong7.coinnews.database.entity.ScrapNewsEntity
import com.hong7.coinnews.database.entity.FilterEntity
import com.hong7.coinnews.database.entity.NewsEntity
import com.hong7.coinnews.database.migration.AutoMigrationSpecs
import com.hong7.coinnews.database.migration.Migration.MIGRATION_1_2

@Database(
    entities = [FilterEntity::class, ScrapNewsEntity::class, NewsEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(
            from = 2,
            to = 3,
            spec = AutoMigrationSpecs.MIGRATION_2_3_SPEC::class
        )
    ],
    exportSchema = true
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
            return Room.databaseBuilder(context, AppDatabase::class.java, "coin_db")
                .addMigrations(MIGRATION_1_2)
                .addTypeConverter(Converter())
                .build()
        }
    }
}
