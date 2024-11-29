package com.sajeg.haka.waka_data_classes

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaData(
    val data: JsonObject
)