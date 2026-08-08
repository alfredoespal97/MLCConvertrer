package com.alma.mlcconvertrer.domain

import com.alma.mlcconvertrer.domain.items.CoinItem
import com.alma.mlcconvertrer.repo.CoinRepository
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(private val coinRepository: CoinRepository) {

    suspend operator fun invoke(): List<CoinItem>{

        return coinRepository.getCoins()
    }

}