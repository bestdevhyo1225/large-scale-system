package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.WishCache

interface WishRedisTransactionRepository {
    fun createWish(wishCache: WishCache): Boolean
}
