package com.android.petopia.data

import android.net.Uri
import java.util.UUID

data class Gallery(
    var title: String,
    var content: String,
    val writer: UserModel,
    var imageUri: Uri,
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis()
)
