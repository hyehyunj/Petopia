package com.djhb.petopia.data.remote

import android.net.Uri
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.UserModel

interface GalleryRepository {
    // 생성, 조회(해당 user의 gallery 전부), 수정, 삭제
    suspend fun createGallery(gallery: GalleryModel): Boolean
    suspend fun selectGalleryList(user: UserModel): MutableList<GalleryModel>
    suspend fun selectGalleryImages(galleryList: MutableList<GalleryModel>) : MutableList<GalleryModel>
    suspend fun selectAllGalleryImages(gallery: GalleryModel) : GalleryModel
    suspend fun updateGallery(gallery: GalleryModel, imageUri: Uri)
    suspend fun deleteGallery(galleryKey: String): Boolean
}