package com.android.petopia.data

data class Memory(
    var title: String,
    var content: String,
    val writer: UserModel,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)
