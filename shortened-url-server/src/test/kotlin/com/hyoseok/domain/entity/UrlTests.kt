package com.hyoseok.domain.entity

import io.kotest.core.spec.style.DescribeSpec
import mu.KotlinLogging

internal class UrlTests : DescribeSpec(
    {
        val logger = KotlinLogging.logger {}

        describe("invoke 메서드는") {
            it("Url 엔티티를 생성한다.") {
                // given
                val url = Url(longUrl = "https://hyos-dev-log.tistory.com/")

                logger.info { url }
            }
        }
    },
)
