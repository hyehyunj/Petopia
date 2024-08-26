package com.android.petopia.presentation.gallery

import android.content.Context
import com.android.petopia.data.GalleryModel

interface GalleryRepository {
    fun getGalleryList(context: Context, galleryList: List<GalleryModel>) : List<GalleryModel>
}