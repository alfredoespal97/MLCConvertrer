package com.alma.mlcconvertrer.data.remote

import com.alma.mlcconvertrer.data.remote.models.CoinModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CoinService @Inject constructor(private val coinApi: CoinApi){

    suspend fun getCoins(): List<CoinModel>{
        return withContext(Dispatchers.IO){
            val coins = coinApi.getCoins()
            coins.body() ?: emptyList()
        }
    }

}