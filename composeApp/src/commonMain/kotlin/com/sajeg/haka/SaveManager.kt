package com.sajeg.haka

import com.russhwolf.settings.Settings


class SaveManager() {
    private val settings: Settings = Settings()

    fun saveString(key: String, value: String) {
        settings.putString(key, value)
    }

    fun loadString(key: String) : String {
        return settings.getString(key, "")
    }
}