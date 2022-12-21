package com.hyoseok.util

import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class PageByPosition<T>(
    val items: List<T>,
    val nextPageRequestByPosition: PageRequestByPosition,
)

data class PageRequestByPosition(
    @field:PositiveOrZero(message = "start는 0보다 같거나 큰 값을 입력하세요")
    val start: Long = 0,

    @field:Positive(message = "size는 0보다 큰 값을 입력하세요")
    val size: Long = 10,
) {
    companion object {
        const val NONE_START = -1L
    }

    fun next(itemSize: Int): PageRequestByPosition {
        if (itemSize < 0) {
            throw IllegalArgumentException("itemSize is negative value")
        }
        val nextStart: Long = if (itemSize == 0 || itemSize < size) NONE_START else start.plus(size)
        return PageRequestByPosition(start = nextStart, size = size)
    }

    fun splitTwoPageRequestByPosition(): Pair<PageRequestByPosition, PageRequestByPosition> {
        if (start <= NONE_START || size == 0L) {
            throw IllegalArgumentException("start, size is invalid")
        }
        val divSize: Long = size.div(other = 2)
        return Pair(
            first = PageRequestByPosition(start = start, size = divSize),
            second = PageRequestByPosition(start = start, size = divSize.plus(size.mod(other = 2))),
        )
    }
}
