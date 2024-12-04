package com.sajeg.haka

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sajeg.haka.screens.Home
import com.sajeg.haka.screens.Leaderboard
import com.sajeg.haka.screens.ProjectOverview
import com.sajeg.haka.screens.SetUp
import kotlinx.serialization.Serializable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = SetUp) {
        composable<Leaderboard> {
            Leaderboard(navController)
        }
        composable<SetUp> {
            SetUp(navController)
        }
        composable<ProjectOverview> {
            ProjectOverview(navController)
        }
        composable<Home> {
            Home(navController)
        }
        composable<ProjectView> {
            val params = it.toRoute<ProjectView>()
//            FileViewer(navController, params.path)
        }
    }
}

@Serializable
object Leaderboard

@Serializable
object ProjectOverview

@Serializable
object SetUp

@Serializable
object Home

@Serializable
data class ProjectView(
    val project: String
)