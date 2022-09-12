package com.hyoseok.config

object RedisKeys {
    const val MEMBER_ZSET_KEY = "member:keys"
    const val PRODUCT_ZSET_KEY = "product:keys"
    const val SNS_ZSET_KEY = "sns:keys"

    fun getMemberKey(id: Long) = "member:$id"
    fun getProductKey(id: Long) = "product:$id"
    fun getSnsKey(id: Long) = "sns:$id"
}
