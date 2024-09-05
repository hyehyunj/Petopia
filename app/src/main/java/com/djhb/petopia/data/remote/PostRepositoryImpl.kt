package com.djhb.petopia.data.remote

import android.util.Log
import androidx.core.net.toUri
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.Table
import com.djhb.petopia.data.PostModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PostRepositoryImpl : PostRepository {

    private val storeReference = Firebase.firestore.collection(Table.QUESTION_POST.tableName)
    private val storageReference = Firebase.storage.getReference(Table.QUESTION_POST.tableName)

    override suspend fun createPost(post: PostModel, imageUris: MutableList<String>): Boolean {
        return suspendCancellableCoroutine { continuation ->
            var isFailToStore = false
            storeReference.document(post.key).set(post).addOnCompleteListener {
                Log.i("PostRepositoryImpl", "success create question post : ${it}")
            }.addOnFailureListener {
                isFailToStore = true
                Log.e("PostRepositoryImpl", "fail create question post : ${it}")
            }

            if (isFailToStore) {
                continuation.resume(false)
                return@suspendCancellableCoroutine
            }

            for ((index, imageUri) in imageUris.withIndex()) {
                val imageName =
                    DateFormatUtils.convertToImageFormat(post.createdDate) + "_0" + (index + 1) + ".png"
                val imageTask = storageReference.child(post.key).child(imageName).putFile(imageUri.toUri())
                imageTask.addOnSuccessListener {
                    Log.i("PostRepositoryImpl", "success create question post image : ${it}")
                }.addOnFailureListener {
                    Log.e("PostRepositoryImpl", "fail create question post image: ${it}")
                    storeReference.document(post.key).delete()
                }
            }
            continuation.resume(true)
        }
    }


    override suspend fun selectRankPosts(): MutableList<PostModel> {
        return suspendCancellableCoroutine { continuation ->

            storeReference
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(4)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("PostRepositoryImpl", "success select rank question post : ${task}")
                        val rankPosts = mutableListOf<PostModel>()
                        for (document in task.result.documents) {
                            val hashMap = document.data as HashMap<*, *>
                            val gson = Gson()
                            val toJson = gson.toJson(hashMap)
                            val fromJson = gson.fromJson(toJson, PostModel::class.java)
                            rankPosts.add(fromJson)
                        }
                        continuation.resume(rankPosts)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception("unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }

        }
    }

    override suspend fun selectPosts(): MutableList<PostModel> {
        return suspendCancellableCoroutine { continuation ->

            storeReference
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("PostRepositoryImpl", "success select question post : ${task}")
                        val rankPosts = mutableListOf<PostModel>()
                        for (document in task.result.documents) {
                            val hashMap = document.data as HashMap<*, *>
                            val gson = Gson()
                            val toJson = gson.toJson(hashMap)
                            val fromJson = gson.fromJson(toJson, PostModel::class.java)
                            rankPosts.add(fromJson)
                        }
                        continuation.resume(rankPosts)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception("unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }

        }
    }

    override suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<PostModel> {
        return suspendCancellableCoroutine { continuation ->
            for (post in posts) {
                storageReference.child(post.key).listAll().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val items = task.result.items
                        if (items.size > 0) {
                            task.result.items[0].downloadUrl.addOnCompleteListener { uri ->
                                post.imageUris.add(uri.result.toString())
                            }
                        }
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception("Unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
            continuation.resume(posts)
        }
    }

    override suspend fun selectPostMainImage(post: PostModel): PostModel {
        return suspendCancellableCoroutine { continuation ->
                storageReference.child(post.key).listAll().addOnCompleteListener { task ->
                    Log.i("PostRepositoryImpl", "success select postMainImage : ${task}")
                    if (task.isSuccessful) {
                        val items = task.result.items
                        if (items.size > 0) {
                            task.result.items[0].downloadUrl.addOnCompleteListener { uri ->
                                Log.i(
                                    "PostRepositoryImpl",
                                    "success select postMainImage uri : ${uri.result}"
                                )
                                post.imageUris.add(uri.result.toString())
                            }
                        }
                        continuation.resume(post)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception("Unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
//            Log.d("PostRepositoryImpl", "selectPostMainImage posts = ${posts}")
        }
    }

    override suspend fun updatePost(post: PostModel) {
        storeReference.document(post.key).set(post).addOnCompleteListener {
            Log.i("PostRepositoryImpl", "success update question post : ${it}")
        }.addOnFailureListener {
            Log.e("PostRepositoryImpl", "fail update question post : ${it}")
        }
    }

    override suspend fun deletePost(key: String) {
        storeReference.document(key).delete().addOnCompleteListener {
            Log.i("PostRepositoryImpl", "success delete question post : ${it}")
        }.addOnFailureListener {
            Log.e("PostRepositoryImpl", "fail delete question post : ${it}")
        }
    }
}
