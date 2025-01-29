package com.sajeg.haka.waka.classes

import kotlinx.serialization.Serializable

@Serializable
sealed class WakaTimeRange {
    abstract var time: String

    data object ALLTIME : WakaTimeRange() {
        override var time = "all_time"
    }

    data object TODAY : WakaTimeRange() {
        override var time = "today"
    }

    data object YESTERDAY : WakaTimeRange() {
        override var time = "yesterday"
    }

    data object WEEK : WakaTimeRange() {
        override var time = "week"
    }

    data object MONTH : WakaTimeRange() {
        override var time = "month"
    }

    data object YEAR : WakaTimeRange() {
        override var time = "year"
    }

    override fun toString(): String {
        return time
    }

    companion object {
        fun fromString(time: String): WakaTimeRange? {
            return when (time) {
                ALLTIME.time -> ALLTIME
                TODAY.time -> TODAY
                YESTERDAY.time -> YESTERDAY
                WEEK.time -> WEEK
                MONTH.time -> MONTH
                YEAR.time -> YEAR
                else -> null
            }
        }
    }
}