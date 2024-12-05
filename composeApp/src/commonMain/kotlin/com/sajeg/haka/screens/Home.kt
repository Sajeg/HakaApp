package com.sajeg.haka.screens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sajeg.haka.SaveManager
import com.sajeg.haka.waka.Wakatime
import com.sajeg.haka.waka.classes.WakaToday
import com.sajeg.haka.waka.classes.WakaTodayData
import com.sajeg.haka.waka.classes.WakaTotalTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

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
            waka.getToday { data ->
                today = data
                today!!.projects.forEach { project ->
                    val jsonProject = Json.decodeFromJsonElement<WakaTodayData>(project)
                    todayProjects.add(jsonProject)
                }
                todayProjects.sortByDescending { it.totalSeconds }
                today!!.languages.forEach { languages ->
                    val jsonLanguage = Json.decodeFromJsonElement<WakaTodayData>(languages)
                    todayLanguages.add(jsonLanguage)
                }
                today!!.editors.forEach { editors ->
                    val jsonEditor = Json.decodeFromJsonElement<WakaTodayData>(editors)
                    todayEditors.add(jsonEditor)
                }
                today!!.machines.forEach { machines ->
                    val jsonMachines = Json.decodeFromJsonElement<WakaTodayData>(machines)
                    todayMachines.add(jsonMachines)
                }
            }
        }
        if (totalTime == null) {
            waka.getAllTime { totalTime = it }
        }
        if (totalTime != null && today != null)
            Column {
                Text("You programmed a total of ${totalTime!!.text}")
                BoxWithConstraints {
                    if (maxWidth > 750.dp) {
                        Row {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.3f)
                            ) {
                                Text("Your top Projects today: ")
                                LazyColumn {
                                    items(todayProjects) { project ->
                                        ProjectCard(project)
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                if (todayLanguages.size > 0) {
                                    Text("Your top languages today: ")
                                    LazyColumn {
                                        items(todayLanguages) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (todayEditors.size > 0) {
                                    Text("Your top Editors today: ")
                                    LazyColumn {
                                        items(todayEditors) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                                if (todayMachines.size > 0) {
                                    Text("Your top Machines today: ")
                                    LazyColumn {
                                        items(todayMachines) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (maxWidth > 500.dp) {
                        Row {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                if (todayProjects.size > 0) {
                                    Text("Your top Projects today: ")
                                    LazyColumn {
                                        items(todayProjects) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                                if (todayMachines.size > 0) {
                                    Text("Your top Machines today: ")
                                    LazyColumn {
                                        items(todayMachines) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                            }
                            Column {
                                if (todayLanguages.size > 0) {
                                    Text("Your top languages today: ")
                                    LazyColumn {
                                        items(todayLanguages) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                                if (todayEditors.size > 0) {
                                    Text("Your top Editors today: ")
                                    LazyColumn {
                                        items(todayEditors) { project ->
                                            ProjectCard(project)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                        ) {
                            if (todayProjects.size > 0) {
                                item {
                                    Text("Your top Projects today: ")
                                }
                                items(todayProjects) { project ->
                                    ProjectCard(project)
                                }
                            }
                            if (todayLanguages.size > 0) {
                                item {
                                    Text("Your top languages today: ")
                                }
                                items(todayLanguages) { project ->
                                    ProjectCard(project)
                                }
                            }
                            if (todayEditors.size > 0) {
                                item {
                                    Text("Your top Editors today: ")
                                }
                                items(todayEditors) { project ->
                                    ProjectCard(project)
                                }
                            }
                            if (todayMachines.size > 0) {
                                item {
                                    Text("Your top Machines today: ")
                                }
                                items(todayMachines) { project ->
                                    ProjectCard(project)
                                }
                            }
                        }
                    }
                }
            }
    }
}

@Composable
fun ProjectCard(project: WakaTodayData) {
    Card(
        modifier = Modifier.padding(5.dp).fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(project.name, style = MaterialTheme.typography.displaySmall)
            Text(project.text, style = MaterialTheme.typography.labelMedium)
        }
    }
}