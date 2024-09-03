package com.android.petopia.data

import java.util.UUID

data class GalleryModel(
    val titleText : String,
    val writer: UserModel,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis(),
    val index: Int = 0,
    var checked : Boolean = false,
    val imageUris: MutableList<String> = mutableListOf(),
    val uId: String = UUID.randomUUID().toString()
)


