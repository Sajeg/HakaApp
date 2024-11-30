package com.sajeg.haka.waka

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaData(
    val data: JsonObject
)