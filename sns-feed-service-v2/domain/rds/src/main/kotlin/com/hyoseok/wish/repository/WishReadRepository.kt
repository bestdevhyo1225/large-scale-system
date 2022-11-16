package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.Wish

interface WishReadRepository {
    fun findById(id: Long): Wish
}
