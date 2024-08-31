package com.android.petopia.data

import android.net.Uri
import java.util.UUID

data class GalleryModel(
    val titleImage : List<Uri>,
    val titleText : String,
    val date : String,
    val index : Int = 0,
    val uId: String = UUID.randomUUID().toString()
)


