package com.bestdev.web.response

data class SuccessResponse<T : Any>(
    val status: String = "success",
    val data: T,
)
