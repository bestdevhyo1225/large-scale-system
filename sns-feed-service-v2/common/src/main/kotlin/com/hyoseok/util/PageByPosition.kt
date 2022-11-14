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
        val nextStart: Long = if (itemSize == 0) NONE_START else start.plus(size)
        return PageRequestByPosition(start = nextStart, size = size)
    }

    fun splitTwoPageRequestByPosition(): Pair<PageRequestByPosition, PageRequestByPosition> {
        if (start <= NONE_START || size == 0L) {
            throw IllegalArgumentException("start, size is invalid")
        }
        val firstStart: Long = start
        val firstSize: Long = size.div(other = 2)
        val secondStart: Long = firstStart.plus(firstSize)
        val secondSize: Long = firstSize.plus(size.mod(other = 2))
        return Pair(
            first = PageRequestByPosition(start = firstStart, size = firstSize),
            second = PageRequestByPosition(start = secondStart, size = secondSize),
        )
    }
}
