package com.sajeg.haka

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "HakaApp",
    ) {
        App()
    }
}