package com.android.petopia.data

import java.util.UUID

data class GalleryModel(
    val titleImage : String,
    val titleText : String,
    val date : String,
    val uId: String = UUID.randomUUID().toString()
)


