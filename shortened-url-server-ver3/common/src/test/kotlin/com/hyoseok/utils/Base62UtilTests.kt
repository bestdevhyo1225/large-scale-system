package com.hyoseok.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldHaveLength

internal class Base62UtilTests : DescribeSpec(
    {

        describe("encode 메서드는") {
            it("Long 타입의 값을 Base62 String 타입의 값으로 인코딩한다.") {
                // given
                val value1: Long = 2009215674938
                val value2: Long = 20092156749382

                // when
                val result1: String = Base62Util.encode(value = value1)
                val result2: String = Base62Util.encode(value = value2)

                result1.shouldHaveLength(7)
                result2.shouldHaveLength(8)
            }
        }
    },
)
