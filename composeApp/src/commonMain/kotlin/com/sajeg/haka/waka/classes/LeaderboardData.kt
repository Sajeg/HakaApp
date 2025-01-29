package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaLeaderboardData(
    @SerialName("daily_average") val dailyAverage: Int,
    @SerialName("human_readable_daily_average") val humanReadableDailyAverage: String,
    @SerialName("human_readable_total") val humanReadableTotal: String,
    val languages: JsonArray,
    @SerialName("total_seconds") val totalSeconds: Int
)