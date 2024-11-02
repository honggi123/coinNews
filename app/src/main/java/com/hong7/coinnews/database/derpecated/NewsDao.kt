//package com.hong7.coinnews.database.derpecated
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Transaction
//import com.hong7.coinnews.database.derpecated.NewsEntity
//
//@Dao
//interface NewsDao {
//
//    @Transaction
//    @Query("SELECT * FROM news")
//    suspend fun getAllNews(): List<NewsEntity>
//
//    @Query("DELETE FROM news")
//    suspend fun deleteAllNews()
//
//    @Insert
//    suspend fun insertAll(newsList: List<NewsEntity>)
//
//}