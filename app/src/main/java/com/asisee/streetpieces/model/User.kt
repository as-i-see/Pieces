package com.asisee.streetpieces.model

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val exists: Boolean = id.isNotEmpty()
)
