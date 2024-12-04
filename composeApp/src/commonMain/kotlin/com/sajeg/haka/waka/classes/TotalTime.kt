package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class WakaTotalTime(
    @SerialName("total_seconds") val totalSeconds: Long,
    val text: String,
    @SerialName("is_up_to_date") val isUpToDate: Boolean,
    val range: JsonObject
)
