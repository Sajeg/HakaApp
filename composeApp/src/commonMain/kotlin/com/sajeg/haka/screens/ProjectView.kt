package com.sajeg.haka.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
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
import androidx.navigation.NavController
import com.sajeg.haka.Home
import com.sajeg.haka.Leaderboard
import com.sajeg.haka.ProjectView
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaProjectData
import com.sajeg.haka.waka.classes.WakaStats
import com.sajeg.haka.waka.classes.WakaTimeRange

@Composable
fun ProjectView(
    navController: NavController,
    timeRange: WakaTimeRange,
    project: String? = null,
    language: String? = null,
    os: String? = null,
    machine: String? = null
) {
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
        var stats by remember { mutableStateOf<WakaStats?>(null) }
        val apiToken by remember { mutableStateOf(SaveManager().loadString("api_token")) }
        val waka = Wakatime(apiToken)
        LaunchedEffect(stats) {
            waka.getStats(timeRange, project, language, os, machine, onFailed = {
            }) { newStats ->
                stats = newStats
            }
        }
        if (stats != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        combineStrings(project, language, os, machine),
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                SingleChoiceSegmentedButtonRow {
                    val buttons = arrayOf(
                        arrayOf("all_time", "All time"),
                        arrayOf("today", "Today"),
                        arrayOf("yesterday", "Yesterday"),
                        arrayOf("week", "Week"),
                        arrayOf("month", "Month"),
                        arrayOf("year", "Year")
                    )
                    for (i in buttons.indices) {
                        SegmentedButton(
                            selected = timeRange.time == buttons[i][0],
                            onClick = {
                                navController.navigate(ProjectView(buttons[i][0], project, os, language, machine))
                            },
                            shape = SegmentedButtonDefaults.itemShape(i, buttons.size)
                        ) {
                            Text(buttons[i][1])
                        }
                    }
                }
                Text("Total time: ${stats!!.humanReadableTotal}")
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

fun combineStrings(vararg strings: String?): String {
    return strings.joinToString(separator = " ") { it ?: "" }
}