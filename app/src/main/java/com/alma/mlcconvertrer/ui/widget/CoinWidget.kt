package com.alma.mlcconvertrer.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.alma.mlcconvertrer.R
import com.alma.mlcconvertrer.data.remote.CoinApi
import com.alma.mlcconvertrer.domain.items.toCoinItem
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.graphics.Color
import androidx.glance.color.ColorProvider

class CoinWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun coinApi(): CoinApi
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPoints.get(context.applicationContext, WidgetEntryPoint::class.java)
        val coinApi = entryPoint.coinApi()

        val coins = try {
            withContext(Dispatchers.IO) {
                val response = coinApi.getCoins()
                response.body()?.map { it.toCoinItem() } ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }

        provideContent {
            CoinWidgetContent(coins)
        }
    }

    @Composable
    private fun CoinWidgetContent(coins: List<com.alma.mlcconvertrer.domain.items.CoinItem>) {
        val context = LocalContext.current
        
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color.White, Color.White))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.market_rates_title),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))

            if (coins.size >= 3) {
                WidgetCoinRow(coins[0], R.drawable.euro, "EUR")
                Spacer(modifier = GlanceModifier.height(4.dp))
                WidgetCoinRow(coins[1], R.drawable.dolar, "USD")
                Spacer(modifier = GlanceModifier.height(4.dp))
                WidgetCoinRow(coins[2], R.drawable.mlc, "MLC")
            } else {
                Text(text = "...")
            }
        }
    }

    @Composable
    private fun WidgetCoinRow(coin: com.alma.mlcconvertrer.domain.items.CoinItem, flag: Int, type: String) {
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                provider = ImageProvider(flag),
                contentDescription = type,
                modifier = GlanceModifier.size(24.dp)
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Text(
                text = type,
                style = TextStyle(fontSize = 12.sp)
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = coin.corner,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
            )
        }
    }
}

class CoinWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CoinWidget()
}
