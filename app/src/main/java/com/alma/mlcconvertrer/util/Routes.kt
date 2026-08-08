package com.alma.mlcconvertrer.util

sealed class Routes(val route: String) {
    data object Main:Routes("main")
    data object Coin:Routes("coin")
    data object Calculate:Routes("calculate")
}