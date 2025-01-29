package com.sajeg.haka.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sajeg.haka.Home
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SetUp(navController: NavController) {
    AppTheme {
        val save = SaveManager()
        var apiToken by remember { mutableStateOf(save.loadString("api_token")) }
        Scaffold {
            if (apiToken == "") {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Welcome to HakaApp",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    SetUpToken { apiToken = it }
                }
            } else {
                navController.navigate(Home)
            }
        }
    }
}


@Composable
fun SetUpToken(onButtonPress: (apiKey: String) -> Unit) {
    val save = SaveManager()
    var apiToken by remember { mutableStateOf(save.loadString("api_token")) }
    var failed by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(15.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Please Enter your api token: ")
            Text("The api token is the same token that you used to set up Hackatime",
                fontStyle = FontStyle.Italic,
                fontSize = 11.sp)
            TextField(
                apiToken,
                { failed = false; apiToken = it },
                modifier = Modifier.width(350.dp)
            )
            if (failed) {
                Text("Invalid Token", color = MaterialTheme.colorScheme.error)
            }
            Button({
                val waka = Wakatime(apiToken)
                waka.getUserData({
                    failed = true
                }) {
                    save.saveString("api_token", apiToken)
                    onButtonPress(apiToken)
                }
            }, modifier = Modifier.padding(15.dp)) {
                Text("Save token")
            }
        }
    }
}