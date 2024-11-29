package com.sajeg.haka.waka_data_classes

import kotlinx.serialization.Serializable

@Serializable
data class WakaProjectData(
    val id: String,
    val name: String,
    val lastHeartbeatAt: String,
    val humanReadableLastHeartbeatAt: String,
    val urlencodedName: String,
    val createdAt: String
)