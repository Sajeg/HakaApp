package com.sajeg.haka.waka

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class WakaArrayData(
    val data: JsonArray
)