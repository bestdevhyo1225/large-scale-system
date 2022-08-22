package com.bestdev.web.response

data class ErrorResponse(
    val status: String = "error",
    val message: String,
)
