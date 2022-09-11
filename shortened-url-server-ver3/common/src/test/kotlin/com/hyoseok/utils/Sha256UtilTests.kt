package com.hyoseok.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class Sha256UtilTests : DescribeSpec(
    {
        describe("encode 메서드는") {
            it("입력 값을 sha-256 알고리즘을 통해 암호화한다.") {
                // given
                val value1 = "https://hyos-dev-log.tistory.com/"
                val value2 = "https://hyos-dev-log.tistory.com/"

                // when
                val result1 = Sha256Util.encode(value = value1)
                val result2 = Sha256Util.encode(value = value2)

                // then
                result1.shouldBe(result2)
            }
        }
    },
)
