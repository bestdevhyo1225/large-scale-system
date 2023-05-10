package com.hyoseok.base.repository

import com.hyoseok.base.entity.Parent
import org.springframework.data.jpa.repository.JpaRepository

interface ParentRepository : JpaRepository<Parent, Long>
