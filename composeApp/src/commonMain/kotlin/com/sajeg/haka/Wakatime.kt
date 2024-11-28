package com.sajeg.haka

import androidx.compose.ui.text.LinkAnnotation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.InternalAPI

class Wakatime(
    private val token: String,
    private val userId: String
) {
    private val apiEndpoint = "https://waka.hackclub.com/api/compat"
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    suspend fun getUserData() : String{
        val response: HttpResponse = client.get("${apiEndpoint}/wakatime/v1/users/current") {
            headers {
                append("Authorization", "Basic $token")
            }
        }
        return response.body<String>()
    }

    suspend fun getAllTime() : String{
        val response: HttpResponse = client.get("${apiEndpoint}/wakatime/v1/users/current/all_time_since_today") {
            headers {
                append("Authorization", "Basic $token")
            }
        }
        return response.body<String>()
    }

    suspend fun getProjects() : String {
        val response: HttpResponse = client.get("${apiEndpoint}/wakatime/v1/users/current/projects") {
            headers {
                append("Authorization", "Basic $token")
            }
        }
        return response.body<String>()
    }
}