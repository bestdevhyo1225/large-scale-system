package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.Wish

interface WishReadRepository {
    fun findBy(id: Long): Wish
}
