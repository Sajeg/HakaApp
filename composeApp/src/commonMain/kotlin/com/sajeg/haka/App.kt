package com.sajeg.haka

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sajeg.haka.waka_data_classes.WakaUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val save = SaveManager()
        var apiToken by remember { mutableStateOf(save.loadString("api_token")) }

        if (apiToken == "") {
            SetUpToken() { apiToken = it }
        } else {
            val waka = Wakatime(apiToken)
            var userData by remember { mutableStateOf<WakaUserData?>(null) }
            waka.getUserData { userData = it}
            if (userData != null) {
                Text(userData!!.email)
            }
        }
    }
}


@Composable
fun SetUpToken( onButtonPress: (apiKey: String) -> Unit) {
    val save = SaveManager()
    var apiToken by remember { mutableStateOf(save.loadString("api_token")) }

    Column {
        Card(
            modifier = Modifier.padding(15.dp),
            backgroundColor = MaterialTheme.colors.secondaryVariant
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text("Please Enter your api token: ")
                TextField(apiToken, { apiToken = it })
            }
            Button({
                save.saveString("api_token", apiToken)
                onButtonPress(apiToken)
            }, modifier = Modifier.padding(15.dp)) {
                Text("Save token")
            }
        }
    }
}