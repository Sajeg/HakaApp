package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaLeaderboardArrayData(
    @SerialName("current_user") val currentUser: WakaCurrentLeaderboardUser,
    val data: JsonArray,
    val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    val language: String,
    val range: JsonObject
)
