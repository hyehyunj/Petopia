package com.android.petopia.data

import java.util.UUID

data class CommentModel(
    val postKey: String,
    val writer: UserModel,
    val content: String,
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)
