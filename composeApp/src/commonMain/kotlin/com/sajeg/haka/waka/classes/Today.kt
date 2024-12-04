package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaToday(
    val categories: JsonArray,
    val dependencies: JsonArray,
    val editors: JsonArray,
    val languages: JsonArray,
    val projects: JsonArray,
    val branches: JsonArray?,
    val entities: JsonArray?,
    @SerialName("grand_total") val grandTotal: JsonObject,
    @SerialName("operating_systems") val operatingSystems: JsonArray,
    val range: JsonObject,
    val machines: JsonArray
)