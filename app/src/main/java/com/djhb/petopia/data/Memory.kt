package com.djhb.petopia.data

import java.util.UUID

data class Memory(
    var title: String,
    var content: String,
    val writer: UserModel,
    var key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)
