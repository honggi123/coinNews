package com.hong7.coinnews.database.migration

import androidx.room.DeleteColumn
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
}