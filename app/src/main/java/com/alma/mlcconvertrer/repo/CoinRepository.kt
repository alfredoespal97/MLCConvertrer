package com.alma.mlcconvertrer.repo

import com.alma.mlcconvertrer.data.remote.CoinService
import com.alma.mlcconvertrer.domain.items.CoinItem
import com.alma.mlcconvertrer.domain.items.toCoinItem
import javax.inject.Inject

class CoinRepository @Inject constructor(private val coinService: CoinService) {
    suspend fun getCoins():List<CoinItem>{

        return  coinService.getCoins().map {
            it.toCoinItem()
        }
    }
}