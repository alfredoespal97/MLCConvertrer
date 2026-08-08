package com.alma.mlcconvertrer.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alma.mlcconvertrer.domain.GetCoinsUseCase
import com.alma.mlcconvertrer.domain.items.CoinItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.withTimeout

sealed class CoinUiState {
    object Loading : CoinUiState()
    data class Success(val coins: List<CoinItem>) : CoinUiState()
    data class Error(val message: String) : CoinUiState()
}

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CoinUiState>(CoinUiState.Loading)
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

    private val _coinSelected = MutableStateFlow("EUR")
    val coinSelected: StateFlow<String> get() = _coinSelected

    private val _value = MutableStateFlow(0.0)
    val value: StateFlow<Double> get() = _value

    private val _isDarkTheme = MutableStateFlow<Boolean?>(null) // null means follow system
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme.asStateFlow()

    init {
        getCoins()
    }

    fun toggleTheme(dark: Boolean?) {
        _isDarkTheme.value = dark
    }

    fun updateCoinSelected(coin: String) {
        _coinSelected.value = coin
        updateValueOnGetCoins()
    }

    private fun updateValue(coin: Double) {
        _value.value = coin
    }

    private fun updateValueOnGetCoins() {
        val currentState = _uiState.value
        if (currentState is CoinUiState.Success && currentState.coins.isNotEmpty()) {
            val newValue = when (_coinSelected.value) {
                "EUR" -> currentState.coins[0].corner.replace(" CUP", "").toDoubleOrNull() ?: 0.0
                "USD" -> currentState.coins[1].corner.replace(" CUP", "").toDoubleOrNull() ?: 0.0
                else -> currentState.coins[2].corner.replace(" CUP", "").toDoubleOrNull() ?: 0.0
            }
            updateValue(newValue)
        }
    }

    fun getCoins() {
        viewModelScope.launch {
            _uiState.value = CoinUiState.Loading
            try {
                // Timeout of 10 seconds for the API call
                val coins = withTimeout(10000L) {
                    withContext(Dispatchers.IO) {
                        getCoinsUseCase()
                    }
                }
                if (coins.isEmpty()) {
                    _uiState.value = CoinUiState.Error("No se encontraron datos")
                } else {
                    _uiState.value = CoinUiState.Success(coins)
                    updateValueOnGetCoins()
                }
            } catch (e: Exception) {
                _uiState.value = CoinUiState.Error("Error al cargar datos: ${e.localizedMessage ?: "Verifique su conexión"}")
            }
        }
    }
}
