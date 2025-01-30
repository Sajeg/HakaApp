package com.sajeg.haka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.GitBranchVisualization
import com.sajeg.haka.ProjectView
import com.sajeg.haka.SaveManager
import com.sajeg.haka.resources.Res
import com.sajeg.haka.resources.back
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaStats
import com.sajeg.haka.waka.classes.WakaTimeRange
import com.sajeg.haka.waka.classes.WakaTodayData
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = Modifier,
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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(combineStrings(project, language, os, machine)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(Res.drawable.back),
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            var innerModifier = Modifier.padding(padding)
            var stats by remember { mutableStateOf<WakaStats?>(null) }
            val apiToken by remember { mutableStateOf(SaveManager().loadString("api_token")) }
            val waka = Wakatime(apiToken)
            if (stats == null) {
                LaunchedEffect(stats) {
                    waka.getStats(timeRange, project, language, os, machine) { newStats ->
                        stats = newStats
                    }
                }
                Column(
                    modifier = innerModifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }

            } else {
                Column(
                    modifier = innerModifier.fillMaxSize()
                ) {
                    val dataToVisualize = mutableListOf(
                        stats!!.editors,
                        stats!!.categories
                    )
                    val labels = mutableListOf(
                        "Editors: ",
                        "Categories: "
                    )
                    if (os == null) {
                        dataToVisualize.add(0, stats!!.operatingSystems)
                        labels.add(0, "Operating systems: ")
                    }
                    if (machine == null) {
                        dataToVisualize.add(0, stats!!.machines)
                        labels.add(0, "Machines: ")
                    }
                    if (project == null) {
                        dataToVisualize.add(0, stats!!.projects)
                        labels.add(0, "Projects: ")
                    }
                    if (language == null) {
                        dataToVisualize.add(0, stats!!.languages)
                        labels.add(0, "Languages: ")
                    }

                    LazyColumn {
                        item {
                            LazyRow {
                                item {
                                    Spacer(Modifier.size(10.dp))
                                }
                                item {
                                    SingleChoiceSegmentedButtonRow {
                                        val buttons = arrayOf(
                                            arrayOf("all_time", "All time"),
                                            arrayOf("today", "Today"),
                                            arrayOf("yesterday", "Yesterday"),
                                            arrayOf("week", "Week"),
                                            arrayOf("month", "Month"),
                                            arrayOf("year", "Year"),
//                                arrayOf("7_days", "7 days"),
                                            arrayOf("last_7_days", "Last 7 days"),
//                                arrayOf("30_days", "30 days"),
                                            arrayOf("last_30_days", "Last 30 days"),
//                                arrayOf("6_months", "6 Months"),
                                            arrayOf("last_6_months", "Last 6 months"),
//                                arrayOf("12_months", "12 months"),
                                        )
                                        for (i in buttons.indices) {
                                            SegmentedButton(
                                                selected = timeRange.time == buttons[i][0],
                                                onClick = {
                                                    navController.navigate(
                                                        ProjectView(
                                                            buttons[i][0],
                                                            project,
                                                            os,
                                                            language,
                                                            machine
                                                        )
                                                    )
                                                },
                                                shape = SegmentedButtonDefaults.itemShape(
                                                    i,
                                                    buttons.size
                                                )
                                            ) {
                                                Text(buttons[i][1])
                                            }
                                        }
                                    }
                                }
                                item {
                                    Spacer(Modifier.size(10.dp))
                                }
                            }
                        }
                        item {
                            GitBranchVisualization(Modifier.fillMaxWidth(), stats!!.branches)
                        }
                        items(dataToVisualize.withIndex().toList()) { (index, data) ->
                            Text(
                                text = labels[index],
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            GeneratePieChart(data, stats!!.humanReadableTotal) { newData ->
                                val labelToParams = mapOf(
                                    "Languages: " to listOf(project, os, newData, machine),
                                    "Projects: " to listOf(newData, os, language, machine),
                                    "Machines: " to listOf(
                                        project, os, language, newData
                                    ),
                                    "Operating Systems:  " to listOf(
                                        project,
                                        newData,
                                        language,
                                        machine
                                    )
                                )

                                labelToParams[labels[index]]?.let { params ->
                                    navController.navigate(
                                        ProjectView(
                                            timeRange.toString(),
                                            project = params[0],
                                            os = params[1],
                                            language = params[2],
                                            machine = params[3],
                                        )
                                    )
                                }
                            }
                        }
                        item {
                            val cardStats = mapOf(
                                "Daily average: " to stats!!.humanReadableDailyAverage,
                                "Days: " to stats!!.daysIncludingHolidays,
                                "Least used Language: " to stats!!.languages.last().name
                            )
                            LazyVerticalGrid(
                                modifier = Modifier.fillParentMaxWidth().height(700.dp),
                                columns = GridCells.Adaptive(150.dp),
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                for (card in cardStats) {
                                    item {
                                        Box(
                                            modifier = Modifier.clip(RoundedCornerShape(15.dp))
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(10.dp)
                                            ) {
                                                Text(
                                                    card.key,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Text(
                                                    card.value.toString(),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun combineStrings(vararg strings: String?): String {
    return strings.joinToString(separator = " ") { it ?: "" }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun GeneratePieChart(stats: List<WakaTodayData>, total: String, onClick: (name: String) -> Unit) {
    val mutableStats = mutableListOf(*stats.toTypedArray())
    val sum = mutableStats.sumOf { it.totalSeconds }
    var other = 0L
    val itemsToRemove = mutableListOf<WakaTodayData>()

    mutableStats.forEach { data ->
        if ((data.totalSeconds.toFloat() / sum.toFloat()) < 0.01) {
            other += data.totalSeconds
            itemsToRemove.add(data)
        }
    }

    mutableStats.removeAll(itemsToRemove)
    if (other > 0) {
        mutableStats.add(
            WakaTodayData(
                digital = "",
                hours = 0,
                minutes = 0,
                name = "Other",
                percent = 0f,
                seconds = 0,
                text = "",
                totalSeconds = other,
            )
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        PieChart(
            values = mutableStats.map { it.totalSeconds.toFloat() },
            label = { i ->
                Column {
                    Text(mutableStats.map { it.name }[i])
                    Text(mutableStats.map { it.text }[i])
                }
            },
            slice = { i ->
                val colors =
                    remember(mutableStats.size) { generateHueColorPalette(mutableStats.size) }
                DefaultSlice(
                    color = colors[i],
                    clickable = true,
                    onClick = { onClick(mutableStats.map { it.name }[i]) }
                )
            },
            holeSize = 0.50F,
            holeContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleLarge)
                        Text(total)
                    }
                }
            }
        )
    }
}