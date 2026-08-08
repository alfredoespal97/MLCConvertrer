package com.alma.mlcconvertrer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.alma.mlcconvertrer.ui.pages.CoinScreen
import com.alma.mlcconvertrer.ui.theme.MLCConvertrerTheme
import com.alma.mlcconvertrer.ui.vm.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val coinViewModel: CoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by coinViewModel.isDarkTheme.collectAsState()
            val actualDark = isDarkTheme ?: isSystemInDarkTheme()

            MLCConvertrerTheme(darkTheme = actualDark) {
                CoinScreen(coinViewModel)
            }
        }
    }
}
