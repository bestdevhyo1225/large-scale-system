package com.hyoseok.util

import javax.validation.constraints.PositiveOrZero

data class PageByPosition<T>(
    val items: List<T>,
    val nextPageRequestByPosition: PageRequestByPosition,
)

data class PageRequestByPosition(
    val start: Long = 0,

    @field:PositiveOrZero(message = "size는 0보다 같거나 큰 값을 입력하세요")
    val size: Long = 10,
) {
    companion object {
        const val NONE_START = -1L
    }

    fun next(start: Long) = PageRequestByPosition(start = start, size = size)
}
