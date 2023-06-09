package com.example.bean

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val msg: String = "",
    val code: Int = -1,
    val result: T? = null
)
