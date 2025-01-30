package com.sajeg.haka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
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
        Scaffold { padding ->
            val innerModifier = Modifier.padding(padding)
            val save = SaveManager()
            val apiToken by remember { mutableStateOf(save.loadString("api_token")) }
            val leaderboard = remember { mutableStateListOf<WakaLeaderboard>() }
            if (leaderboard.isEmpty()) {
                val waka = Wakatime(apiToken)
                waka.getLeaderboard { leaderboardData, currentUser ->
                    leaderboardData.forEach { leaderboard.add(it) }
                    val user = leaderboard.last()
                    leaderboard.remove(user)
                    leaderboard.add(0, user)
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    item {
                        Text(
                            "You",
                            modifier = Modifier.safeDrawingPadding()
                                .padding(start = 5.dp, bottom = 5.dp)
                        )
                    }
                    item { scoreCard(leaderboard.first()) }
                    item { Text("1. - 100.", modifier = Modifier.padding(5.dp)) }
                    items(leaderboard.subList(1, leaderboard.size)) { score ->
                        scoreCard(score)
                    }
                }
            }
        }
    }
}

@Composable
fun scoreCard(data: WakaLeaderboard) {
    Row(
        modifier = Modifier.padding(5.dp).clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primaryContainer).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.size(5.dp))
        AsyncImage(
            modifier = Modifier.size(40.dp).clip(CircleShape),
            model = data.user.photo,
            contentDescription = "User avatar",
        )
        Spacer(Modifier.size(10.dp))
        Column {
            Text("${data.rank}. ${data.user.displayName}")
            Text(
                "Total ${data.runningTotal.humanReadableTotal} • Daily avg: ${data.runningTotal.humanReadableDailyAverage}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}