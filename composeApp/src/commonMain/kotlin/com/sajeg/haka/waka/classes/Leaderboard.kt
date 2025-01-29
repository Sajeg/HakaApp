package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaLeaderboard(
    val rank: Int,
    @SerialName("running_total") val runningTotal: WakaLeaderboardData,
    val user: WakaUserData
)
