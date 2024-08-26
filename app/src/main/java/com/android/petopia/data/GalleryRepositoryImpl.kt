package com.android.petopia.data

import android.content.Context
import com.android.petopia.presentation.gallery.GalleryRepository


class GalleryRepositoryImpl(private val galleryModel: List<GalleryModel>) :
    GalleryRepository {
    override fun getGalleryList(context: Context, galleryList: List<GalleryModel>): List<GalleryModel> {
        GalleryDataSource.getGalleryDataSource().saveGalleryList(context,galleryList)
        GalleryDataSource.getGalleryDataSource().loadGalleryList(context)
        return galleryModel
    }
}