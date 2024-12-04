package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaAltData(
    @SerialName("cached_at") val cachedAt: String,
    val data: JsonObject
)