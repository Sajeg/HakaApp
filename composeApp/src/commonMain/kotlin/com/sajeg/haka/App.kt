package com.sajeg.haka

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sajeg.haka.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    lateinit var navController: NavHostController
    AppTheme {
        Surface {
            navController = rememberNavController()
            SetupNavGraph(navController = navController)
        }
    }
}