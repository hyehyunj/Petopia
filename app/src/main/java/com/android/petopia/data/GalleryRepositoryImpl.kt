package com.android.petopia.data

import android.content.Context
import com.android.petopia.presentation.gallery.GalleryRepository


class GalleryRepositoryImpl :
    GalleryRepository {
    override fun updateGalleryList(context: Context, galleryList: List<GalleryModel>): List<GalleryModel> {
        GalleryDataSource.getGalleryDataSource().saveGalleryList(context,galleryList)
        val updateGalleryList = GalleryDataSource.getGalleryDataSource().loadGalleryList(context)
        return updateGalleryList
    }

//    override fun updateGalleryList(context: Context, photo: GalleryModel): List<GalleryModel> {
//
//        val mutableList = GalleryDataSource.getGalleryDataSource().loadGalleryList(context).toMutableList()
//        val index = mutableList.indexOf(mutableList.find{ it.titleImage == photo.titleImage })
//if(index == -1) mutableList.add(photo) else mutableList[index] = photo
//        return mutableList.toList()
//    }
}