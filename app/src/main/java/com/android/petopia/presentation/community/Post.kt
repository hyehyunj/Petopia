package com.android.petopia.presentation.community

data class Post(
    val index: Int = 0,
    var title: String = "",
    var content: String = "",
    val writer: User = User(),
    var viewCount: Int = 0 ,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis(),
)
