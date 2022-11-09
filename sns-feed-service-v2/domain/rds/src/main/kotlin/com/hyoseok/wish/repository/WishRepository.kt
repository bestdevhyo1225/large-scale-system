package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository

interface WishRepository : JpaRepository<Wish, Long>
