package com.sajeg.haka.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.Home
import com.sajeg.haka.Leaderboard
import com.sajeg.haka.ProjectOverview
import com.sajeg.haka.ProjectView
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaProjectData
import com.sajeg.haka.waka.classes.WakaTimeRange
import com.sajeg.haka.waka.classes.WakaTodayData

enum class NavigationDestinations(
    val label: String,
    val icon: ImageVector,
    val destination: String,
    val destinationLink: Any
) {
    HOME("Home", Icons.Rounded.Home, "com.sajeg.haka.Home", Home),
    PROJECT(
        "Projects",
        Icons.AutoMirrored.Rounded.List,
        "com.sajeg.haka.ProjectOverview",
        ProjectOverview
    ),
    LEADERBOARD("Leaderboard", Icons.Rounded.Leaderboard, "com.sajeg.haka.Leaderboard", Leaderboard)
}

@Composable
fun ProjectOverview(navController: NavController) {
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
        val projects = remember { mutableListOf<WakaProjectData>() }
        val apiToken by remember { mutableStateOf(SaveManager().loadString("api_token")) }
        var update by remember { mutableStateOf("Start") }
        val waka = Wakatime(apiToken)
        LaunchedEffect(Unit) {
            update = "Connecting"
            waka.getProjects({
                update = "Failed"
                val text = "Lol"
            }
            ) { project ->
                update = "Success"
                projects.addAll(project)
            }
        }
        Column {
            if (update != "Success") {
                Text(update, color = MaterialTheme.colorScheme.surface)
            }
            if (projects.isNotEmpty()) {
                LazyColumn {
                    items(projects) {
                        ProjectCard(it, navController)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: WakaProjectData, navController: NavController) {
    Card(
        modifier = Modifier.padding(5.dp).fillMaxSize().clickable {
            navController.navigate(ProjectView(timeRange = WakaTimeRange.ALLTIME.toString(), project = project.id))
        }
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(project.name, style = MaterialTheme.typography.displaySmall)
            Text(project.humanReadableLastHeartbeatAt, style = MaterialTheme.typography.labelMedium)
        }
    }
}