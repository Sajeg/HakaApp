package com.sajeg.haka.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.sajeg.haka.Home
import com.sajeg.haka.Leaderboard
import com.sajeg.haka.ProjectOverview

enum class NavigationDestinations(
    val label: String,
    val icon: ImageVector,
    val destination: String,
    val destinationLink: Any
) {
    HOME("Home", Icons.Rounded.Home, "com.sajeg.haka.Home", Home),
    PROJECT("Projects", Icons.AutoMirrored.Rounded.List,"com.sajeg.haka.ProjectOverview", ProjectOverview),
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
        Text("Welcome to Project Overview")
    }
}