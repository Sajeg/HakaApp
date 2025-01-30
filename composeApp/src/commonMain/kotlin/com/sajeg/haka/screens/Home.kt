package com.sajeg.haka.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.GitBranchVisualization
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaTimeRange
import com.sajeg.haka.waka.classes.WakaToday
import com.sajeg.haka.waka.classes.WakaTodayData
import com.sajeg.haka.waka.classes.WakaTotalTime
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.autoScaleXRange
import io.github.koalaplot.core.xygraph.autoScaleYRange
import io.github.koalaplot.core.xygraph.rememberFloatLinearAxisModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Home(navController: NavController) {
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
            val waka = Wakatime(apiToken)
            var totalTime by remember { mutableStateOf<WakaTotalTime?>(null) }
            var today by remember { mutableStateOf<WakaToday?>(null) }
            val todayProjects = remember { mutableStateListOf<WakaTodayData>() }
            val todayMachines = remember { mutableStateListOf<WakaTodayData>() }
            val todayEditors = remember { mutableStateListOf<WakaTodayData>() }
            val todayLanguages = remember { mutableStateListOf<WakaTodayData>() }

            if (today == null) {
                Column(
                    modifier = innerModifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
                waka.getToday { data ->
                    today = data
                    todayProjects.removeAll(todayProjects)
                    today!!.projects.forEach { project ->
                        val jsonProject = Json.decodeFromJsonElement<WakaTodayData>(project)
                        todayProjects.add(jsonProject)
                    }
                    todayProjects.sortByDescending { it.totalSeconds }
                    todayLanguages.removeAll(todayLanguages)
                    today!!.languages.forEach { languages ->
                        val jsonLanguage = Json.decodeFromJsonElement<WakaTodayData>(languages)
                        todayLanguages.add(jsonLanguage)
                    }
                    todayEditors.removeAll(todayEditors)
                    today!!.editors.forEach { editors ->
                        val jsonEditor = Json.decodeFromJsonElement<WakaTodayData>(editors)
                        todayEditors.add(jsonEditor)
                    }
                    todayMachines.removeAll(todayMachines)
                    today!!.machines.forEach { machines ->
                        val jsonMachines = Json.decodeFromJsonElement<WakaTodayData>(machines)
                        todayMachines.add(jsonMachines)
                    }
                }
                waka.getAllTime { totalTime = it }
            }
            if (totalTime != null && today != null)
                LazyVerticalGrid(
                    modifier = innerModifier.fillMaxSize(),
                    columns = GridCells.Adaptive(250.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        TodayCard(
                            WakaTodayData(
                                name = totalTime!!.text,
                                text = "total hours of programming",
                                digital = "",
                                hours = 0,
                                minutes = 0,
                                percent = 0.9f,
                                seconds = 0,
                                totalSeconds = 0
                            ),
                            true
                        ) {}
                    }
                    item {
                        if (todayProjects.size > 0) {
                            Column {
                                Text("Your top Projects today: ")
                                for (project in todayProjects) {
                                    TodayCard(project) {
                                        navController.navigate(
                                            com.sajeg.haka.ProjectView(
                                                timeRange = WakaTimeRange.ALL_TIME.toString(),
                                                project = project.name
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        if (todayLanguages.size > 0) {
                            Column {
                                Text("Your top languages today: ")
                                for (language in todayLanguages) {
                                    TodayCard(language) {
                                        navController.navigate(
                                            com.sajeg.haka.ProjectView(
                                                timeRange = WakaTimeRange.ALL_TIME.toString(),
                                                language = language.name
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        if (todayEditors.size > 0) {
                            Column {
                                Text("Your top Editors today: ")
                                for (editor in todayEditors) {
                                    TodayCard(editor) {
                                        navController.navigate(
                                            com.sajeg.haka.ProjectView(
                                                timeRange = WakaTimeRange.ALL_TIME.toString(),
                                                editor = editor.name
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        if (todayMachines.size > 0) {
                            Column {
                                Text("Your top Machines today: ")
                                for (machine in todayMachines) {
                                    TodayCard(machine) {
                                        navController.navigate(
                                            com.sajeg.haka.ProjectView(
                                                timeRange = WakaTimeRange.ALL_TIME.toString(),
                                                machine = machine.name
                                            )
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

@Composable
fun TodayCard(project: WakaTodayData, center: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(5.dp).fillMaxSize().clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(15.dp).fillMaxWidth(),
            horizontalAlignment = if (center) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(project.name, style = MaterialTheme.typography.displaySmall)
            Text(project.text, style = MaterialTheme.typography.labelMedium)
        }
    }
}