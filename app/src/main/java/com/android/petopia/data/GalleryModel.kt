package com.android.petopia.data

import java.util.UUID

data class GalleryModel(
    val titleImage : String,
    val titleText : String,
    val writer: UserModel,
    val createdDate : Long,
    var updatedDate: Long,
    val index: Int = 0,
    val imageUris: MutableList<String> = mutableListOf(),
    val uId: String = UUID.randomUUID().toString()
)


