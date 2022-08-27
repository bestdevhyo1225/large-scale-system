package com.hyoseok.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class Crc32UtilTests : DescribeSpec(
    {
        describe("encode 메서드는") {
            it("CRC32 해시 값을 생성한다.") {
                // given
                val value = "https://en.wikipedia.org/wiki/Systems_design"

                // when
                val result: String = Crc32Util.encode(value = value)

                // then
                result.shouldBe("5cb54054")
            }
        }
    },
)
