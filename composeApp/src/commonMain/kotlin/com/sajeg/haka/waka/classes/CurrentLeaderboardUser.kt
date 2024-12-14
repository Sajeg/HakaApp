package com.sajeg.haka.waka.classes

import kotlinx.serialization.Serializable

@Serializable
data class WakaCurrentLeaderboardUser(
    val page: Int,
    val rank: Int,
    val user: WakaUserData
)
