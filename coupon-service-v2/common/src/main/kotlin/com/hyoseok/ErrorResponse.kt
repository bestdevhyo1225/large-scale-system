package com.hyoseok

data class ErrorResponse(
    val status: String = "error",
    val message: String,
)
