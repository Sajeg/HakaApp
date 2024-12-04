package com.sajeg.haka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.ProjectOverview
import com.sajeg.haka.SaveManager
import com.sajeg.haka.Wakatime
import com.sajeg.haka.waka.WakaProjectData
import com.sajeg.haka.waka.WakaTotalTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SetUp(navController: NavController) {
    val save = SaveManager()
    var apiToken by remember { mutableStateOf(save.loadString("api_token")) }

    Column(
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        if (apiToken == "") {
            SetUpToken { apiToken = it }
        } else {
            val waka = Wakatime(apiToken)
            val projectData = remember { mutableStateListOf<WakaProjectData>() }
            var totalTime by remember { mutableStateOf<WakaTotalTime?>(null) }
            waka.getProjects { projects -> projects.forEach { projectData.add(it) } }
            waka.getAllTime { totalTime = it }
            if (projectData.isNotEmpty() && totalTime != null) {
                navController.navigate(ProjectOverview)
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
        backgroundColor = MaterialTheme.colors.secondaryVariant
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text("Please Enter your api token: ")
            TextField(
                apiToken,
                { failed = false; apiToken = it },
                modifier = Modifier.width(350.dp)
            )
            if (failed) {
                Text("Invalid Token", color = MaterialTheme.colors.error)
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