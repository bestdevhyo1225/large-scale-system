package com.hyoseok.base.entity

import java.io.Serializable

data class ChildPk(
    val id: Long? = null,
    val parent: Parent? = null,
) : Serializable
