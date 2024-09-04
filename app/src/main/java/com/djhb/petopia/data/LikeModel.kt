package com.djhb.petopia.data

import java.util.UUID

data class LikeModel(
    val postKey: String,
    val user: UserModel,
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)
