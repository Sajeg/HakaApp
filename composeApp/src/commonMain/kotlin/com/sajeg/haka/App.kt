package com.sajeg.haka

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            var apiToken by remember { mutableStateOf("") }
            var userId by remember { mutableStateOf("") }

            Column {
                Card(
                    modifier = Modifier.padding(15.dp),
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Column(
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text("Please Enter your api token: ")
                        TextField(apiToken, { apiToken = it })
                        Spacer(modifier = Modifier.height(50.dp))
                        Text("Please Enter your User ID: ")
                        TextField(userId, { userId = it })
                    }
                }
                Button({
                    CoroutineScope(Dispatchers.Default).launch {
                        Wakatime(
                            apiToken,
                            userId
                        ).getUserData()
                    }
                }, modifier = Modifier.padding(15.dp)) {
                    Text("Save information")
                }
            }
        }
    }
}