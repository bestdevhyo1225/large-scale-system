package com.hyoseok

import com.hyoseok.util.PageRequestByPosition
import com.hyoseok.util.PageRequestByPosition.Companion.NONE_START
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class PageRequestByPositionTests : DescribeSpec(
    {
        describe("next 메서드는") {
            context("itemSize 값이 0보다 크지만 size 보다 작은 경우") {
                it("next start 값은 -1을 반환한다") {
                    // given
                    val start = 0L
                    val size = 10L
                    val pageRequestByPosition = PageRequestByPosition(start = start, size = size)

                    // when
                    val nextPageRequestByPosition: PageRequestByPosition = pageRequestByPosition.next(itemSize = 1)

                    // then
                    nextPageRequestByPosition.start.shouldBe(NONE_START)
                }
            }

            context("itemSize 값이 0인 경우") {
                it("NONE_START 상수 값을 반환한다") {
                    // given
                    val start = 0L
                    val size = 10L
                    val pageRequestByPosition = PageRequestByPosition(start = start, size = size)

                    // when
                    val nextPageRequestByPosition: PageRequestByPosition = pageRequestByPosition.next(itemSize = 0)

                    // then
                    nextPageRequestByPosition.start.shouldBe(NONE_START)
                }
            }

            context("itemSize 값이 0보다 작은 경우") {
                it("IllegalArgumentException 예외를 던진다") {
                    shouldThrow<IllegalArgumentException> { PageRequestByPosition().next(itemSize = -1) }
                }
            }
        }

        describe("splitTwoPageRequestByPosition 메서드는") {
            it("start, size 값으로 2개의 PageRequestByPosition 객체를 생성한다") {
                // given
                val start = 0L
                val size = 10L
                val pageRequestByPosition = PageRequestByPosition(start = start, size = size)

                // when
                val (
                    firstPageRequestByPosition: PageRequestByPosition,
                    secondPageRequestByPosition: PageRequestByPosition,
                ) = pageRequestByPosition.splitTwoPageRequestByPosition()

                // then
                firstPageRequestByPosition.start.shouldBe(expected = 0)
                firstPageRequestByPosition.size.shouldBe(expected = 5)
                secondPageRequestByPosition.start.shouldBe(expected = 0)
                secondPageRequestByPosition.size.shouldBe(expected = 5)
            }

            it("여러가지 start, size 값 테스트") {
                listOf(
                    Pair(first = PageRequestByPosition(), second = listOf<Long>(0, 5, 0, 5)),
                    Pair(first = PageRequestByPosition(start = 10, size = 10), second = listOf<Long>(10, 5, 10, 5)),
                    Pair(first = PageRequestByPosition(start = 20, size = 10), second = listOf<Long>(20, 5, 20, 5)),
                    Pair(first = PageRequestByPosition(start = 30, size = 10), second = listOf<Long>(30, 5, 30, 5)),
                    Pair(first = PageRequestByPosition(start = 40, size = 10), second = listOf<Long>(40, 5, 40, 5)),
                    Pair(first = PageRequestByPosition(start = 50, size = 10), second = listOf<Long>(50, 5, 50, 5)),
                    Pair(first = PageRequestByPosition(start = 50, size = 20), second = listOf<Long>(50, 10, 50, 10)),
                    Pair(first = PageRequestByPosition(start = 50, size = 30), second = listOf<Long>(50, 15, 50, 15)),
                    Pair(first = PageRequestByPosition(start = 0, size = 20), second = listOf<Long>(0, 10, 0, 10)),
                    Pair(first = PageRequestByPosition(start = 20, size = 20), second = listOf<Long>(20, 10, 20, 10)),
                    Pair(first = PageRequestByPosition(start = 5, size = 5), second = listOf<Long>(5, 2, 5, 3)),
                    Pair(first = PageRequestByPosition(start = 15, size = 15), second = listOf<Long>(15, 7, 15, 8)),
                ).forEach {
                    // given
                    val (pageRequestByPosition: PageRequestByPosition, expecteds: List<Long>) = it

                    // when
                    val (
                        firstPageRequestByPosition: PageRequestByPosition,
                        secondPageRequestByPosition: PageRequestByPosition,
                    ) = pageRequestByPosition.splitTwoPageRequestByPosition()

                    // then
                    firstPageRequestByPosition.start.shouldBe(expected = expecteds[0])
                    firstPageRequestByPosition.size.shouldBe(expected = expecteds[1])
                    secondPageRequestByPosition.start.shouldBe(expected = expecteds[2])
                    secondPageRequestByPosition.size.shouldBe(expected = expecteds[3])
                }
            }
        }
    },
)
