package com.alma.mlcconvertrer.ui.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.alma.mlcconvertrer.R
import com.alma.mlcconvertrer.domain.items.CoinItem
import com.alma.mlcconvertrer.ui.theme.MLCConvertrerTheme
import com.alma.mlcconvertrer.ui.vm.CoinUiState
import com.alma.mlcconvertrer.ui.vm.CoinViewModel

@Composable
fun CoinScreen(
    coinViewModel: CoinViewModel = hiltViewModel()
) {
    val uiState by coinViewModel.uiState.collectAsState()
    val isDarkTheme by coinViewModel.isDarkTheme.collectAsState()
    val coinSelected by coinViewModel.coinSelected.collectAsState()
    val valorText by coinViewModel.value.collectAsState()

    CoinScreenContent(
        uiState = uiState,
        isDarkTheme = isDarkTheme,
        coinSelected = coinSelected,
        valorText = valorText,
        onToggleTheme = { coinViewModel.toggleTheme(it) },
        onRetry = { coinViewModel.getCoins() },
        onCoinSelected = { coinViewModel.updateCoinSelected(it) }
    )
}

@Composable
fun CoinScreenContent(
    uiState: CoinUiState,
    isDarkTheme: Boolean?,
    coinSelected: String,
    valorText: Double,
    onToggleTheme: (Boolean?) -> Unit,
    onRetry: () -> Unit,
    onCoinSelected: (String) -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val actualDark = isDarkTheme ?: systemDark

    MLCConvertrerTheme(darkTheme = actualDark) {
        val showDialog = remember { mutableStateOf(false) }
        var isCupToForeign by remember { mutableStateOf(true) }
        var resultado by remember { mutableStateOf(0.0) }
        var texto by remember { mutableStateOf(TextFieldValue("")) }

        if (showDialog.value) {
            InfoDialog(setShowDialog = { showDialog.value = it })
        }

        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                    elevation = 0.dp,
                    actions = {
                        IconButton(onClick = {
                            val next = when (isDarkTheme) {
                                true -> false
                                false -> null
                                null -> true
                            }
                            onToggleTheme(next)
                        }) {
                            Icon(
                                imageVector = when (isDarkTheme) {
                                    true -> Icons.Default.WbSunny
                                    false -> Icons.Default.NightlightRound
                                    null -> Icons.Default.SettingsBrightness
                                },
                                contentDescription = "Theme"
                            )
                        }
                        IconButton(onClick = { showDialog.value = true }) {
                            Icon(Icons.Default.Info, contentDescription = "Info")
                        }
                    }
                )
            },
            backgroundColor = MaterialTheme.colors.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.billetes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.1f,
                    contentScale = ContentScale.Crop
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            backgroundColor = MaterialTheme.colors.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(R.string.market_rates_title),
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                when (val state = uiState) {
                                    is CoinUiState.Loading -> {
                                        Column {
                                            repeat(3) { SkeletonItem() }
                                        }
                                    }
                                    is CoinUiState.Success -> {
                                        val coins = state.coins
                                        if (coins.size >= 3) {
                                            CoinRow(coins[0], R.drawable.euro, "EUR")
                                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                                            CoinRow(coins[1], R.drawable.dolar, "USD")
                                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                                            CoinRow(coins[2], R.drawable.mlc, "MLC")
                                        }
                                    }
                                    is CoinUiState.Error -> {
                                        ErrorState(
                                            message = state.message,
                                            onRetry = onRetry
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            backgroundColor = MaterialTheme.colors.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CurrencySelector(selected = coinSelected, onCoinSelected = onCoinSelected)

                                Spacer(modifier = Modifier.height(20.dp))

                                if (uiState is CoinUiState.Success) {
                                    ConverterSection(
                                        isCupToForeign = isCupToForeign,
                                        coinSelected = coinSelected,
                                        texto = texto,
                                        resultado = resultado,
                                        onTextoChange = { newText ->
                                            texto = newText
                                            resultado = calculateResult(newText.text, valorText, isCupToForeign)
                                        },
                                        onSwap = {
                                            isCupToForeign = !isCupToForeign
                                            resultado = calculateResult(texto.text, valorText, isCupToForeign)
                                        }
                                    )
                                } else if (uiState is CoinUiState.Loading) {
                                    Box(Modifier.height(200.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

fun calculateResult(input: String, rate: Double, isCupToForeign: Boolean): Double {
    if (input.isEmpty() || rate == 0.0) return 0.0
    val amount = input.toDoubleOrNull() ?: return 0.0
    val res = if (isCupToForeign) amount / rate else amount * rate
    return round2Decimals(res, 2)
}

@Composable
fun SkeletonItem() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .height(20.dp)
                .weight(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = alpha))
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colors.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, color = MaterialTheme.colors.error)
        TextButton(onClick = onRetry) {
            Text(stringResource(R.string.retry_button))
        }
    }
}

@Composable
fun CoinRow(coin: CoinItem, flag: Int, type: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = flag),
                contentDescription = type,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("1 $type", fontWeight = FontWeight.Medium)
        }
        Text(
            coin.corner,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ConverterSection(
    isCupToForeign: Boolean,
    coinSelected: String,
    texto: TextFieldValue,
    resultado: Double,
    onTextoChange: (TextFieldValue) -> Unit,
    onSwap: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isCupToForeign) "CUP" else coinSelected, fontWeight = FontWeight.Bold)
            IconButton(onClick = onSwap, modifier = Modifier.padding(horizontal = 16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_compare), 
                    contentDescription = "Swap"
                )
            }
            Text(if (isCupToForeign) coinSelected else "CUP", fontWeight = FontWeight.Bold)
        }

        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            label = { Text(stringResource(R.string.amount_label)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
            elevation = 0.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.result_label), style = MaterialTheme.typography.caption)
                Text(
                    "$resultado ${if (isCupToForeign) coinSelected else "CUP"}",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun CurrencySelector(selected: String, onCoinSelected: (String) -> Unit) {
    val options = listOf("EUR", "USD", "MLC")
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(
                        id = when (selected) {
                            "EUR" -> R.drawable.euro
                            "USD" -> R.drawable.dolar
                            else -> R.drawable.mlc
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.currency_selector_label, selected))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onCoinSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun InfoDialog(setShowDialog: (Boolean) -> Unit) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.about_title), style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { setShowDialog(false) }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.about_description_1))
                Text(stringResource(R.string.about_description_2))
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { setShowDialog(false) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.close_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoinScreenPreview() {
    val sampleCoins = listOf(
        CoinItem("350.0 CUP"),
        CoinItem("340.0 CUP"),
        CoinItem("330.0 CUP")
    )
    CoinScreenContent(
        uiState = CoinUiState.Success(sampleCoins),
        isDarkTheme = false,
        coinSelected = "EUR",
        valorText = 350.0,
        onToggleTheme = {},
        onRetry = {},
        onCoinSelected = {}
    )
}
