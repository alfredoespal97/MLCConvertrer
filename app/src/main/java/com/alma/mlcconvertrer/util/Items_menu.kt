package com.alma.mlcconvertrer.util

import com.alma.mlcconvertrer.R

sealed class Items_menu(
    val icons: Int,
    val title: String,
    val ruta: String
){
    object ScreenCalculate: Items_menu(
        R.drawable.calculator,"Calculate",Routes.Calculate.route
    )
    object ScreenCoin: Items_menu(
        R.drawable.coin,"Coin",Routes.Coin.route
    )
}
