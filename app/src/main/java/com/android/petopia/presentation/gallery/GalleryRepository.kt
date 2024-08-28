package com.android.petopia.presentation.gallery

import android.content.Context
import com.android.petopia.data.GalleryModel

interface GalleryRepository {
    fun updateGalleryList(context: Context, galleryList: List<GalleryModel>) : List<GalleryModel>

    fun updateGalleryList(context: Context, photo: GalleryModel) : List<GalleryModel>

}