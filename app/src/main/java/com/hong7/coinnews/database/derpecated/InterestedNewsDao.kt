//package com.hong7.coinnews.database.derpecated
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Transaction
//import com.hong7.coinnews.database.derpecated.ScrapNewsEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface InterestedNewsDao {
//
//    @Query("SELECT EXISTS(SELECT 1 FROM scrap_news WHERE news_id = :newsId LIMIT 1)")
//    fun isInterested(newsId: String): Flow<Boolean>
//
//    @Transaction
//    @Query("SELECT * FROM scrap_news")
//    fun fetchAllNews(): Flow<List<ScrapNewsEntity>>
//
//    @Insert
//    suspend fun insert(newsEntity: ScrapNewsEntity)
//
//    @Query("DELETE FROM scrap_news WHERE news_id = :id")
//    suspend fun delete(id: String)
//}