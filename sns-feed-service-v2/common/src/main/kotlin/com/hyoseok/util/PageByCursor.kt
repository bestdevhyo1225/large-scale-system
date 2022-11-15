package com.hyoseok.util

data class PageByCursor<T>(
    val items: List<T>,
    val nextPageRequestByCursor: PageRequestByCursor,
)

data class PageRequestByCursor(
    val key: Long = 0,
    val size: Long = 10,
) {

    fun next(key: Long) = PageRequestByCursor(key = key, size = size)
}
