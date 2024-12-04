package com.sajeg.haka

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    lateinit var navController: NavHostController
    MaterialTheme {
        navController = rememberNavController()
        SetupNavGraph(navController = navController)
    }
}