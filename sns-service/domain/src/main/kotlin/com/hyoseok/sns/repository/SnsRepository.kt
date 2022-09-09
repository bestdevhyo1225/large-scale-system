package com.hyoseok.sns.repository

import com.hyoseok.sns.entity.Sns

interface SnsRepository {
    fun save(sns: Sns)
}
