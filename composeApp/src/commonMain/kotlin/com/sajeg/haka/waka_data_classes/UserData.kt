package com.sajeg.haka.waka_data_classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WakaUserData(
    val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("full_name") val fullName: String,
    val email: String,
    @SerialName("is_email_public") val isEmailPublic: Boolean,
    @SerialName("is_email_confirmed") val isEmailConfirmed: Boolean,
    val timezone: String,
    @SerialName("last_heartbeat_at") val lastHeartbeatAt: String,
    @SerialName("last_project") val lastProject: String,
    @SerialName("last_plugin_name") val lastPluginName: String,
    val username: String,
    val website: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("modified_at") val modifiedAt: String,
    val photo: String
)