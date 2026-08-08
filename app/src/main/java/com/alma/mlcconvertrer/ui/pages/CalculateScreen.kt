package com.alma.mlcconvertrer.ui.pages


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alma.mlcconvertrer.datastore.CoinStore
import com.alma.mlcconvertrer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import com.alma.mlcconvertrer.ui.theme.Surface
import com.alma.mlcconvertrer.util.Routes
import okhttp3.Route

@Composable
fun CalculateScreen(){
    var texto by remember { mutableStateOf(TextFieldValue("")) }

    val cup="CUP"
    val mlc="MLC"
    var coin by remember { mutableStateOf(true) }
    var resultado by remember { mutableDoubleStateOf(0.0) }
    var colorText = Surface
    var valorError by remember { mutableStateOf(false) } // 1


    val context = LocalContext.current
    val store = CoinStore(context)
    val valorText = store.getCoin.collectAsState(initial = "")
    var valor by remember {  mutableStateOf(TextFieldValue(valorText.value)) }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.billetes), contentDescription ="imagen", modifier = Modifier
            .fillMaxSize()
            .scale(1.3f),alpha = 0.4F )
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size = 20.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                        Text("Precio de 1 MLC = ${valorText.value} CUP", color = colorText)
                        Spacer(modifier =  Modifier.height(6.dp))
                        OutlinedTextField(value = valor, onValueChange =  { it ->
                            valor = it
                            valorError = false
                            CoroutineScope(Dispatchers.IO).launch {
                                store.saveCoin(valor.text)
                            }
                        },textStyle = TextStyle(textAlign = TextAlign.Center),
                            singleLine = true,
                            isError = valorError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        val assistiveElementText = if (valorError) "Error: Obligatorio" else "*Obligatorio" // 4
                        val assistiveElementColor = if (valorError) { // 5
                            MaterialTheme.colors.error
                        } else {
                            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                        }
                        Text(// 6
                            text = assistiveElementText,
                            color = assistiveElementColor,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size = 20.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(15.dp)) {
                        Image(painter = painterResource(id = R.drawable.mlc), contentDescription ="imagen" )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if(coin)cup else mlc, color = colorText)
                            IconButton(onClick = {
                                coin=!coin
                                resultado = if(texto.text==""){
                                    0.0
                                }else{
                                    if(coin){
                                        val div: Double=texto.text.toDouble()/valorText.value.toDouble()
                                        round2Decimals(div,2)
                                    }else{
                                        val div: Double=texto.text.toDouble()*valorText.value.toDouble()
                                        round2Decimals(div,2)
                                    }
                                }
                                // if(coin)
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_compare), contentDescription ="" )
                            }
                            Text(if(coin)mlc else cup, color = colorText)
                        }
                        OutlinedTextField(value = texto, onValueChange =  { it ->
                            var value:String=it.text
                            if(valorText.value.isBlank()){
                                valorError=valor.text.isBlank();
                            }else{
                                if(it.text==""){
                                    resultado= 0.0
                                }else{
                                    value.removeSuffix(if (coin)" CUP" else " MLC")
                                    if(coin){
                                        var div: Double=value.toDouble()/valorText.value.toDouble()
                                        resultado= round2Decimals(div,2)
                                    }else{
                                        var div: Double=value.toDouble()*valorText.value.toDouble()
                                        resultado=round2Decimals(div,2)
                                    }
                                }
                            }
                            texto = it
                        },
                            visualTransformation = SuffixTransformation(if (coin)" CUP" else " MLC"),
                            label = { Text(text = "Valor a convertir") },
                            placeholder = { Text(text = "monto a convertir") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if(coin) Row(verticalAlignment = Alignment.Bottom) {
                            Text("$resultado ",  color = colorText, fontSize = 30.sp,
                                //  fontWeight = FontWeight.ExtraBold
                            )
                            Text("MLC",  color = colorText, fontSize = 16.sp,
                                //  fontWeight = FontWeight.ExtraBold
                            )
                        } else
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("$resultado ",  color = colorText, fontSize = 30.sp,
                                    //  fontWeight = FontWeight.ExtraBold
                                )
                                Text("CUP",  color = colorText, fontSize = 15.sp,
                                    //  fontWeight = FontWeight.ExtraBold
                                )
                            }
                    }
                }

            }
        }
    }
}


fun round2Decimals(number: Double, numDecimalPlaces: Int): Double {
    return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
}

class SuffixTransformation(val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val result = text + AnnotatedString(suffix)

        val textWithSuffixMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (text.isEmpty()) return 0
                if (offset >=  text.length) return text.length
                return offset
            }
        }

        return TransformedText(result, textWithSuffixMapping )
    }
}