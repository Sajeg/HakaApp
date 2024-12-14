package com.sajeg.haka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaCurrentLeaderboardUser
import com.sajeg.haka.waka.classes.WakaLeaderboard

@Composable
fun Leaderboard(navController: NavController) {
    val currentDestination = navController.currentDestination?.route ?: ""
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            NavigationDestinations.entries.forEach {
                item(
                    icon = { Icon(it.icon, "") },
                    label = { Text(it.label) },
                    selected = it.destination == currentDestination,
                    onClick = {
                        navController.navigate(it.destinationLink)
                    }
                )
            }
        }
    ) {
        val save = SaveManager()
        val apiToken by remember { mutableStateOf(save.loadString("api_token")) }
        val leaderboard = remember { mutableStateListOf<WakaLeaderboard>() }
        var user by remember { mutableStateOf<WakaCurrentLeaderboardUser?>(null) }
        if (leaderboard.isEmpty()) {
            Text("Loading... Please wait a few seconds")
            val waka = Wakatime(apiToken)
            waka.getLeaderboard { leaderboardData, currentUser ->
                leaderboardData.forEach { leaderboard.add(it) }
                user = currentUser
            }
        } else {
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(15.dp)).padding(5.dp)
                    ) {
                        Text("${user!!.rank} • You")
                    }
                }
                item { Text("1. - 100.") }
                for (score in leaderboard) {
                    item { scoreCard(score) }
                }
            }
        }
    }
}

@Composable
fun scoreCard(data: WakaLeaderboard) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(15.dp)).padding(5.dp).background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text("${data.rank} ${data}")
    }
}