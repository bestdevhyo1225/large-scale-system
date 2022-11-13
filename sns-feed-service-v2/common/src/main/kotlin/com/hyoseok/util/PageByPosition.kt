package com.hyoseok.util

data class PageByPosition<T>(
    val items: List<T>,
    val nextPageRequestByPosition: PageRequestByPosition,
)

data class PageRequestByPosition(
    val start: Long = 0,
    val size: Long = 10,
) {
    companion object {
        const val NONE_START = -1L
    }

    fun next(start: Long) = PageRequestByPosition(start = start, size = size)
}
