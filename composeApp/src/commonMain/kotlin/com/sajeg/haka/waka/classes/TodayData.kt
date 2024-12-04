package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WakaTodayData(
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val name: String,
    val percent: Float,
    val seconds: Int,
    val text: String,
    @SerialName("total_seconds") val totalSeconds: Long
)