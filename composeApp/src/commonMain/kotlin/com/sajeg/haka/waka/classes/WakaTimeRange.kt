package com.sajeg.haka.waka.classes

import kotlinx.serialization.Serializable

@Serializable
sealed class WakaTimeRange {
    abstract var time: String

    data object ALL_TIME : WakaTimeRange() {
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

    data object SEVEN_DAYS : WakaTimeRange() {
        override var time = "7_days"
    }

    data object LAST_SEVEN_DAYS : WakaTimeRange() {
        override var time = "last_7_days"
    }

    data object THIRTY_DAYS : WakaTimeRange() {
        override var time = "30_days"
    }

    data object LAST_THIRTY_DAYS : WakaTimeRange() {
        override var time = "last_30_days"
    }

    data object SIX_MONTHS : WakaTimeRange() {
        override var time = "6_months"
    }

    data object LAST_SIX_MONTHS : WakaTimeRange() {
        override var time = "last_6_months"
    }

    data object TWELVE_MONTHS : WakaTimeRange() {
        override var time = "12_months"
    }

    data object LAST_TWELVE_MONTHS : WakaTimeRange() {
        override var time = "last_12_months"
    }

    override fun toString(): String {
        return time
    }

    companion object {
        fun fromString(time: String): WakaTimeRange? {
            return when (time) {
                ALL_TIME.time -> ALL_TIME
                TODAY.time -> TODAY
                YESTERDAY.time -> YESTERDAY
                WEEK.time -> WEEK
                MONTH.time -> MONTH
                YEAR.time -> YEAR
                SEVEN_DAYS.time -> SEVEN_DAYS
                LAST_SEVEN_DAYS.time -> LAST_SEVEN_DAYS
                THIRTY_DAYS.time -> THIRTY_DAYS
                LAST_THIRTY_DAYS.time -> LAST_THIRTY_DAYS
                SIX_MONTHS.time -> SIX_MONTHS
                LAST_SIX_MONTHS.time -> LAST_SIX_MONTHS
                TWELVE_MONTHS.time -> TWELVE_MONTHS
                LAST_TWELVE_MONTHS.time -> LAST_TWELVE_MONTHS
                else -> null
            }
        }
    }
}