package com.djhb.petopia.data.remote

import android.net.Uri
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.UserModel
import com.google.firebase.storage.StorageReference

interface GalleryRepository {
    // 생성, 조회(해당 user의 gallery 전부), 수정, 삭제
    suspend fun createGallery(gallery: GalleryModel): Boolean
    suspend fun selectGalleryList(user: UserModel): MutableList<GalleryModel>
    // suspend fun selectGalleryMainImages(galleryList: MutableList<GalleryModel>) : MutableList<GalleryModel>
    suspend fun selectGalleryMainImages(galleryUid : String) : StorageReference?
    suspend fun selectAllGalleryImages(gallery: GalleryModel) : GalleryModel
    suspend fun selectDownloadUri(item: StorageReference): String
    suspend fun updateGallery(gallery: GalleryModel, imageUri: Uri)
    suspend fun deleteGallery(galleryKey: String): Boolean
}
