package com.hyoseok.config

import com.hyoseok.RedisHashSlotCalculator
import com.hyoseok.exception.Message.CANNOT_USED_HASH_TAG
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class RedisHashSlotCalculatorTests : DescribeSpec(
    {
        describe("getHashSlot 메서드는") {
            it("key에 대한 해시 슬롯을 알려준다") {
                // given
                val key = "somekey"

                // when
                val hashSlot = RedisHashSlotCalculator.getHashSlot(key = key)

                // then
                hashSlot.shouldBe(11058)
            }

            context("해시 태그의 Key인 경우에는") {
                it("예외를 던진다") {
                    val key = "foo{hash_tag}"

                    val exception = shouldThrow<IllegalArgumentException> {
                        RedisHashSlotCalculator.getHashSlot(key = key)
                    }

                    exception.localizedMessage.shouldBe(CANNOT_USED_HASH_TAG)
                }
            }
        }
    },
)
