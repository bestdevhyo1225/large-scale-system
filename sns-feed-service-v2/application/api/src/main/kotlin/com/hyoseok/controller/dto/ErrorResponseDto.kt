package com.hyoseok.controller.dto

data class ErrorResponseDto(
    val status: String = "error",
    val message: String,
)
