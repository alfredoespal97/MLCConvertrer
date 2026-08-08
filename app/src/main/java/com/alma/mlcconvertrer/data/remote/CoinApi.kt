package com.alma.mlcconvertrer.data.remote


import com.alma.mlcconvertrer.BuildConfig
import com.alma.mlcconvertrer.data.remote.models.CoinModel
import retrofit2.Response
import retrofit2.http.GET

interface CoinApi {

    @GET(BuildConfig.API_BASE_URL)
    suspend fun getCoins():Response<List<CoinModel>>
}