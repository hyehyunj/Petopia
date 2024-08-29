package com.android.petopia.presentation.community

data class User(
    val id: String = "",
    var password: String = "",
    var name: String = "",
    var nickname: String = "",
    var email: String = "",
    var isCompletedGuide: Boolean = false,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis(),
    var authority: Authority = Authority.CLIENT
)
