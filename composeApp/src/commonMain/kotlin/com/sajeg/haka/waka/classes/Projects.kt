package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WakaProjectData(
    val id: String,
    val name: String,
    @SerialName("last_heartbeat_at") val lastHeartbeatAt: String,
    @SerialName("human_readable_last_heartbeat_at") val humanReadableLastHeartbeatAt: String,
    @SerialName("urlencoded_name") val urlencodedName: String,
    @SerialName("created_at") val createdAt: String
)