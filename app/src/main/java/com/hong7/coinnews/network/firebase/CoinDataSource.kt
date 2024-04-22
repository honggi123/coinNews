package com.hong7.coinnews.network.firebase

import android.net.Network
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hong7.coinnews.network.model.NetworkCoin
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class CoinDataSource {

    private val firestore = Firebase.firestore

    suspend fun getAllCoins(): List<NetworkCoin> =
        collection("Coin")
            .get()
            .await().toObjects(NetworkCoin::class.java)

    suspend fun getUpdatedCoins(lastUpdate: Calendar): List<NetworkCoin> =
        collection("Coin")
            .whereGreaterThanOrEqualTo("updatedAt", lastUpdate.time)
            .get()
            .await().toObjects(NetworkCoin::class.java)

    private fun collection(path: String) = firestore.collection(path)
}
