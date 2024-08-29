package com.android.petopia.data

data class PostModel(
    val index: Int = 0,
    var title: String = "",
    var content: String = "",
    val writer: UserModel = UserModel(),
    var viewCount: Int = 0,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null,
)
