package com.example.currancy

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.currancy.model.CurrancyBO
import com.example.currancy.model.RateBO
import com.example.currancy.ui.theme.CurrancyTheme
import com.example.currancy.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                contontent()
            }

            /* Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.background
               ) {

                    CurrencyView(curList = mainViewModel.currencyListResponse)
                   mainViewModel.getCurrencyList("EUR")
                }
               contontent()*/
        }
    }
}


@Composable
fun CurrencyView(curList: List<RateBO>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    LazyColumn {
        itemsIndexed(items = curList) { index, item ->
            CurrencyItem(item, index, selectedIndex) { i ->
                selectedIndex = i
            }
        }
    }
}


@Composable
fun CurrencyItem(curr: RateBO, index: Int, selectedIndex: Int, onClick: (Int) -> Unit) {
    isLoading.value = false
    Surface(color = Color.Black) {
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxSize()
        ) {

            Text(
                text = countryFlag(curr.cur), fontSize = 30.sp, modifier = Modifier
                    .padding(5.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
                    .weight(0.8f)
            ) {
                Text(
                    text = curr.cur,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = curr.curVal.toString(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

    }
}

@ExperimentalMaterial3Api
@Composable
fun contontent() {

    var mDisplayMenu by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("USD") }
    // fetching local context
    val mContext = LocalContext.current

    // Creating a Top bar
    Column {
        TopAppBar(
            title = { Text(mSelectedText, color = Color.White) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black),
            actions = {
                // Creating Icon button for dropdown menu
                IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                    Icon(Icons.Default.MoreVert, "")
                }

                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("USD") },
                        onClick = {
                            mDisplayMenu = false
                            mSelectedText = "USD"
                            apiCall("USD")
                            isLoading.value = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("EUR") },
                        {
                            mDisplayMenu = false
                            mSelectedText = "EUR"
                            apiCall("EUR")
                            isLoading.value = true
                        }

                    )
                    DropdownMenuItem(
                        text = { Text("GBP") },
                        onClick = {
                            mDisplayMenu = false
                            mSelectedText = "GBP"
                            apiCall("GBP")
                            isLoading.value = true
                        }
                    )

                }
            }

        )

        if (isLoading.value)
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Surface(
            color = Color.Black, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Currency Exchange", fontSize = 30.sp, color = Color.White)
        }
        CurrencyView(curList = mainViewModel.currencyListResponse)
        mainViewModel.getCurrencyList("USD")

    }


}

private var isLoading = mutableStateOf(true)

//custom lineindicator
@Composable
private fun CustomLinearProgressBar() {
    Column(modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            //Background = Color.LightGray,
            color = Color.Blue //progress color
        )
    }
}

val mainViewModel = MainViewModel()
fun apiCall(cur: String) {
    mainViewModel.getCurrencyList(cur)
}

fun countryFlag(code: String) = code.take(2)
    .uppercase()
    .split("")
    .filter { it.isNotBlank() }
    .map { it.codePointAt(0) + 0x1F1A5 }
    .joinToString("") { String(Character.toChars(it)) }


