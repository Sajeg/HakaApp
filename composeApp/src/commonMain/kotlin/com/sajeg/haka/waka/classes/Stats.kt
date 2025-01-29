package com.sajeg.haka.waka.classes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WakaStats(
    val username: String,
    @SerialName("user_id") val userId: String,
    val start: String,
    val end: String,
    val status: String,
    @SerialName("total_seconds") val totalSeconds: Long,
    @SerialName("daily_average") val dailyAverage: Double,
    @SerialName("days_including_holidays") val daysIncludingHolidays: Int,
    val range: String,
    @SerialName("human_readable_range") val humanReadableRange: String,
    @SerialName("human_readable_total") val humanReadableTotal: String,
    @SerialName("human_readable_daily_average") val humanReadableDailyAverage: String,
    @SerialName("is_coding_activity_visible") val isCodingActivityVisible: Boolean,
    @SerialName("is_other_usage_visible") val isOtherUsageVisible: Boolean,
    val editors: List<WakaTodayData>,
    val languages: List<WakaTodayData>,
    val machines: List<WakaTodayData>,
    val projects: List<WakaTodayData>,
    @SerialName("operating_systems") val operatingSystems: List<WakaTodayData>,
    val branches: List<WakaTodayData> = emptyList(),
    val categories: List<WakaTodayData>
)