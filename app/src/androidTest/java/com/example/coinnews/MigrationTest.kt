package com.example.coinnews

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.hong7.coinnews.database.dao.AppDatabase
import com.hong7.coinnews.database.migration.AutoMigrationSpecs
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue


class MigrationsTest<ContentValues> {
    private val testDatabase = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        listOf(AutoMigrationSpecs.MIGRATION_2_3_SPEC()),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        var db = helper.createDatabase(testDatabase, 2)
        db.execSQL("INSERT INTO scrap_news (id, news_id, title, description, url, author, put_date) VALUES (1, 'news_001', '뉴스 이름', '뉴스 설명', 'http://example.com', '한겨례 신문', '2023-05-25')")
        db.close()

        // Act
        db = helper.runMigrationsAndValidate(testDatabase, 3, true)

        val cursor = db.query("SELECT * FROM scrap_news")
        assertTrue(cursor.moveToFirst())
        assertEquals(1, cursor.getInt(cursor.getColumnIndex("id")))
        assertEquals("news_001", cursor.getString(cursor.getColumnIndex("news_id")))
        assertEquals("뉴스 이름", cursor.getString(cursor.getColumnIndex("title")))
        assertEquals("http://example.com", cursor.getString(cursor.getColumnIndex("url")))
        assertEquals("한겨례 신문", cursor.getString(cursor.getColumnIndex("author_name")))
        assertEquals("2023-05-25", cursor.getString(cursor.getColumnIndex("put_date")))

        assertFalse(cursor.getColumnIndex("description") != -1)

        cursor.close()
    }


//    @Test
//    @Throws(IOException::class)
//    fun testAllMigrations() {
//
//    }
}