package com.sajeg.haka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform