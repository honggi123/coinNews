package com.hong7.coinnews.database.migration

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

class AutoMigrationSpecs {

    @RenameColumn.Entries(
        RenameColumn(
            tableName = "scrap_news",
            fromColumnName = "author",
            toColumnName = "author_name"
        )
    )
    @DeleteColumn.Entries(
        DeleteColumn(tableName = "scrap_news", columnName = "description"),
    )
    class MIGRATION_2_3_SPEC : AutoMigrationSpec

    @DeleteTable("news")
    @DeleteTable("scrap_news")
    @DeleteTable("filter")
    class MIGRATION_3_4_SPEC : AutoMigrationSpec

    @DeleteTable("coin")
    @DeleteTable("watchlist")
    class MIGRATION_4_5_SPEC : AutoMigrationSpec
}