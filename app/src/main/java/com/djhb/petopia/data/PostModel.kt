package com.djhb.petopia.data

import java.util.UUID

data class PostModel(
    val title: String,
    val content: String,
    val writer: UserModel,
    val imageUris: MutableList<String> = mutableListOf(),
    val key: String = UUID.randomUUID().toString(),
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null,
)
