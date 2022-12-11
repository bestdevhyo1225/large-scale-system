package com.hyoseok.feed.entity

class Feed {
    companion object {
        const val ZSET_FEED_MAX_LIMIT = -101L

        fun getMemberIdFeedsKey(id: Long) = "member:$id:feeds"
        fun getMemberIdFeedsKeyAndExpireTime(id: Long) =
            Pair(first = getMemberIdFeedsKey(id = id), second = 60 * 30L) // 30분 -> PostCache의 만료시간과 동일하게
    }
}
