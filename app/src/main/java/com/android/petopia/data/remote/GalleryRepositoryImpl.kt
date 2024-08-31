package com.android.petopia.data.remote

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.android.petopia.DateFormatUtils
import com.android.petopia.Table
import com.android.petopia.data.Gallery
import com.android.petopia.data.GalleryModel
import com.android.petopia.data.UserModel
import com.android.petopia.network.FirebaseReference
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GalleryRepositoryImpl: GalleryRepository {

    private val RTReference = FirebaseReference.reference.child(Table.GALLERY.tableName)
    private val storageReference = Firebase.storage.getReference(Table.GALLERY.tableName)


    override suspend fun createGallery(gallery: GalleryModel): Boolean {

        return suspendCancellableCoroutine { continuation ->
            // RT
            var isFailRT = false
            var isFailStorage = false
            val imageName = DateFormatUtils.convertToImageFormat(gallery.createdDate) + "_01.png"
            RTReference.child(gallery.uId).setValue(gallery).addOnFailureListener {
//                continuation.resumeWithException(it)
                isFailRT = true
//                continuation.resume(false)
                continuation.resumeWithException(Exception("fail create gallery"))
            }
            // 다중 사진 업로드 시 수정(번호 증가)
            for ((index, imageUri) in gallery.imageUris.withIndex()) {
                val imageName = DateFormatUtils.convertToImageFormat(gallery.createdDate) + "_0"+(index+1)+".png"
                val uploadTask = storageReference.child(gallery.uId).child(imageName).putFile(imageUri.toUri())
                uploadTask.addOnFailureListener {
                    isFailStorage = false
                    if(!isFailRT) {
                        RTReference.child(gallery.uId).removeValue()
//                    continuation.resume(false)
                        continuation.resumeWithException(Exception("fail create gallery"))
                    }
                }
            }


            if(isFailRT || isFailStorage)
                continuation.resumeWithException(Exception("create gallery fail"))
            continuation.resume(true)
            return@suspendCancellableCoroutine

            // storage


        }

//        storageReference.child(gallery.key).
    }

    override suspend fun selectGalleryList(user: UserModel): MutableList<GalleryModel> {
        return suspendCancellableCoroutine { continuation ->
            Log.i("GalleryRepositoryImpl", "start selectGalleryList")
            val galleryList = mutableListOf<GalleryModel>()

            RTReference.orderByChild("writer/id").equalTo(user.id).get().addOnCompleteListener {
                val result = it.result
                Log.i("GalleryRepositoryImpl", "result count = ${result.childrenCount}")
                for (child in result.children) {
                    val value = child.value as HashMap<*,*>
                    val gson = Gson()
                    val toJson = gson.toJson(value)
                    val fromJson = gson.fromJson(toJson, GalleryModel::class.java)
                    galleryList.add(fromJson)
                }
                Log.i("GalleryRepositoryImpl", "galleryList.size = ${galleryList.size}")
                continuation.resume(galleryList)
                return@addOnCompleteListener
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun selectGalleryImages(galleryList: MutableList<GalleryModel>): MutableList<GalleryModel> {
        return suspendCancellableCoroutine { continuation ->
            for (gallery in galleryList) {
                val key = gallery.uId
                Log.i("GalleryRepositoryImpl", "gallery key = ${key}")
                storageReference.child(key).listAll().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val items = task.result.items
                        Log.i("GalleryRepositoryImpl", "item size = ${items.size}")
                        for (item in items) {
//                            Log.d("GalleryRepositoryImpl", "${item.name}")
                            item.downloadUrl.addOnCompleteListener{ uri ->
                                gallery.imageUris.add(uri.result.toString())
                            }
                            break // 리스트에 출력되는 대표 이미지 1개만 추가
                        }
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
                continuation.resume(galleryList)
            }
        }
    }

    override suspend fun selectAllGalleryImages(gallery: GalleryModel): GalleryModel {
        return suspendCancellableCoroutine { continuation ->
            val key = gallery.uId
            Log.i("GalleryRepositoryImpl", "gallery key = ${key}")
            storageReference.child(key).listAll().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val items = task.result.items
                    Log.i("GalleryRepositoryImpl", "item size = ${items.size}")
                    for (item in items) {
//                            Log.d("GalleryRepositoryImpl", "${item.name}")
                        item.downloadUrl.addOnCompleteListener{ uri ->
                            gallery.imageUris.add(uri.result.toString())
                        }
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                }
                continuation.resume(gallery)
                return@addOnCompleteListener
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun updateGallery(gallery: GalleryModel, imageUri: Uri) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGallery(galleryKey: String): Boolean {
        return suspendCancellableCoroutine { continuation ->

//            RTReference.child(galleryKey).removeValue().addOnCompleteListener{
//
//            }.addOnFailureListener {
//                continuation.resumeWithException(it)
//            }

//            storageReference.(galleryKey+"/").delete().addOnCompleteListener{
            Log.i("GalleryRepositoryImpl", "gallery key = ${galleryKey}")
            storageReference.child(galleryKey).listAll().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val items = task.result.items
                    Log.i("GalleryRepositoryImpl", "item size = ${items.size}")
                    for (item in items) {
//                            Log.d("GalleryRepositoryImpl", "${item.name}")
//                        item.downloadUrl.addOnCompleteListener{ uri ->
//                            gallery.imageUris.add(uri.result)
//                        }
                        Log.i("GalleryRepositoryImpl", "item name = ${item.name}")
                        storageReference.child(galleryKey+"/"+item.name).delete()
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                }
                continuation.resume(true)
                return@addOnCompleteListener
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }

        }
    }
}