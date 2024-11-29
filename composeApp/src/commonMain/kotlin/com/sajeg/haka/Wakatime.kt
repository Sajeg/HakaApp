package com.sajeg.haka

import com.sajeg.haka.waka_data_classes.WakaData
import com.sajeg.haka.waka_data_classes.WakaProjectData
import com.sajeg.haka.waka_data_classes.WakaTotalTime
import com.sajeg.haka.waka_data_classes.WakaUserData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class Wakatime(
    private val token: String,
) {
    private val apiEndpoint = "https://waka.hackclub.com/api/compat"
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    fun getUserData(onResponse: (response: WakaUserData) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val response: HttpResponse = client.get("${apiEndpoint}/wakatime/v1/users/current") {
                headers {
                    append("Authorization", "Basic $token")
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val body = response.body<String>()
                val dataBody = Json.decodeFromString<WakaData>(body)
                onResponse(Json.decodeFromString<WakaUserData>(dataBody.data.toString()))
            }
        }
    }

    fun getAllTime(onResponse: (response: WakaTotalTime) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val response: HttpResponse =
                client.get("${apiEndpoint}/wakatime/v1/users/current/all_time_since_today") {
                    headers {
                        append("Authorization", "Basic $token")
                    }
                }
            if (response.status == HttpStatusCode.OK) {
                val body = response.body<String>()
                val dataBody = Json.decodeFromString<WakaData>(body)
                onResponse(Json.decodeFromString<WakaTotalTime>(dataBody.data.toString()))
            }
        }
    }

    fun getProjects(onResponse: (response: WakaProjectData) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val response: HttpResponse =
                client.get("${apiEndpoint}/wakatime/v1/users/current/projects") {
                    headers {
                        append("Authorization", "Basic $token")
                    }
                }
            if (response.status == HttpStatusCode.OK) {
                val body = response.body<String>()
                val dataBody = Json.decodeFromString<WakaData>(body)
                onResponse(Json.decodeFromString<WakaProjectData>(dataBody.data.toString()))
            }
        }
    }
}