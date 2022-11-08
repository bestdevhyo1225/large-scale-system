package com.hyoseok

data class SuccessResponse<T : Any>(
    val status: String = "success",
    val data: T,
)
