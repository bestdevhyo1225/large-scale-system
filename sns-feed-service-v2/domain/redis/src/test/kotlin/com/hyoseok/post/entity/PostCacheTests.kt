package com.hyoseok.post.entity

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class PostCacheTests : DescribeSpec(
    {
        describe("getPostBucketKey 메서드는") {
            it("id를 HASH_MAX_ENTRIES 기준으로 나눠서 저장한다") {
                // given
                val id = 1239851L

                // when
                val postBuckKey: String = PostCache.getPostBucketKey(id = id)

                // then
                postBuckKey.shouldBe("postbucket:1239")
            }
        }
    },
)
