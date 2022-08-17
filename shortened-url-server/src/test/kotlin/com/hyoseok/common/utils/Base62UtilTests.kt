package com.hyoseok.common.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class Base62UtilTests : DescribeSpec(
    {
        describe("encode 메서드는") {
            it("62진법을 통해 해시 값으로 변환한다.") {
                // given
                val value = 2009215674938

                // when
                val hashValue = Base62Util.encode(value = value)

                // then
                hashValue.shouldBe("zn9edcu")
            }
        }
    },
)
