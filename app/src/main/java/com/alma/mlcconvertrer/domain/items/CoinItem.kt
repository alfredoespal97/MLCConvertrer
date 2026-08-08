package com.alma.mlcconvertrer.domain.items

import com.alma.mlcconvertrer.data.remote.models.CoinModel

data class CoinItem(
    val corner: String
)

fun CoinModel.toCoinItem() = CoinItem(corner)