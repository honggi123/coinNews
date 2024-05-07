package com.hong7.coinnews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestedNewsDao {

    @Query("SELECT EXISTS(SELECT 1 FROM news WHERE news_id = :newsId LIMIT 1)")
    fun isInterested(newsId: String): Flow<Boolean>

    @Transaction
    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Insert
    suspend fun insert(newsEntity: NewsEntity)

    @Query("DELETE FROM news WHERE news_id = :id")
    suspend fun delete(id: String)
}