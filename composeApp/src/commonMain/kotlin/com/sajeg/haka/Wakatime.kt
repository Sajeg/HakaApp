package com.sajeg.haka

import com.sajeg.haka.waka.WakaArrayData
import com.sajeg.haka.waka.WakaData
import com.sajeg.haka.waka.WakaProjectData
import com.sajeg.haka.waka.WakaTotalTime
import com.sajeg.haka.waka.WakaUserData
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

    fun getUserData(onFailed: () -> Unit = {},onResponse: (response: WakaUserData) -> Unit) {
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
            } else {
                onFailed()
            }
        }
    }

    fun getAllTime(onFailed: () -> Unit = {},onResponse: (response: WakaTotalTime) -> Unit) {
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
            } else {
                onFailed()
            }
        }
    }

    fun getProjects(onFailed: () -> Unit = {}, onResponse: (response: Array<WakaProjectData>) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            val response: HttpResponse =
                client.get("${apiEndpoint}/wakatime/v1/users/current/projects") {
                    headers {
                        append("Authorization", "Basic $token")
                    }
                }
            if (response.status == HttpStatusCode.OK) {
                val body = response.body<String>()
                val dataBody = Json.decodeFromString<WakaArrayData>(body)
                onResponse(Json.decodeFromString<Array<WakaProjectData>>(dataBody.data.toString()))
            } else {
                onFailed()
            }
        }
    }
}