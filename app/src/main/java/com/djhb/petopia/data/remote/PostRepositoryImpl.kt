package com.djhb.petopia.data.remote

import android.util.Log
import androidx.core.net.toUri
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.FilteringType
import com.djhb.petopia.Table
import com.djhb.petopia.data.PostModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PostRepositoryImpl(val table: Table) : PostRepository {

    private val storeReference = Firebase.firestore.collection(table.tableName)
    private val storageReference = Firebase.storage.getReference(table.tableName)

//    override suspend fun createPost(post: PostModel, imageUris: MutableList<String>): PostModel {
//        return suspendCancellableCoroutine { continuation ->
//            var isFailToStore = false
//            storeReference.document(post.key).set(post).addOnCompleteListener {
//                Log.i("PostRepositoryImpl", "success create question post : ${it.result}")
//            }.addOnFailureListener {
//                isFailToStore = true
//                Log.e("PostRepositoryImpl", "fail create question post : ${it}")
//                continuation.resumeWithException(it)
//                return@addOnFailureListener
//            }
//
//            if (isFailToStore)
//                continuation.resumeWithException(Exception("fail create post"))
//
//
//            CoroutineScope(Dispatchers.Default).launch {
//                createPostImages(post, imageUris)
//            }
//
////            continuation.resume(post)
//        }
//    }

    override suspend fun createPost(post: PostModel, imageUris: MutableList<String>) {
        withContext(Dispatchers.IO) {
            var isFailToStore = false
            storeReference.document(post.key).set(post).await()

//            if (isFailToStore)
//                return@withContext

//                createPostImages(post, imageUris)


        }

    }

    override suspend fun createPostImages(post: PostModel, imageUris: MutableList<String>): Boolean {
        return withContext(Dispatchers.IO) {
            for ((index, imageUri) in imageUris.withIndex()) {
                val imageName =
                    DateFormatUtils.convertToImageFormat(post.createdDate) + "_0" + (index + 1) + ".png"
                Log.i("PostRepositoryImpl", "createdDate = ${post.createdDate}")
                Log.i("PostRepositoryImpl", "uri = ${imageUri}")
                Log.i(
                    "PostRepositoryImpl",
                    "createdDateForamt = ${DateFormatUtils.convertToImageFormat(post.createdDate)}"
                )
                storageReference
                    .child(post.key)
                    .child(imageName)
                    .putFile(imageUri.toUri())
                    .await()
            }
            true
        }
    }

    override suspend fun selectRankPosts(): MutableList<PostModel> {
        return withContext(Dispatchers.IO) {

            val snapshot = storeReference
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .await()

            val rankPosts = mutableListOf<PostModel>()
            for (document in snapshot.documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, PostModel::class.java)
                rankPosts.add(fromJson)
            }

            Log.i("PostRepositoryImpl", "rankPosts = ${rankPosts}")

            rankPosts

        }
    }

    override suspend fun selectRankPostsWhereFiltering(categories: List<FilteringType>): MutableList<PostModel> {
        return withContext(Dispatchers.IO) {

            val snapshot = storeReference
                .whereArrayContainsAny("filteringType", categories)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .await()

            val rankPosts = mutableListOf<PostModel>()
            for (document in snapshot.documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, PostModel::class.java)
                rankPosts.add(fromJson)
            }

            Log.i("PostRepositoryImpl", "rankPosts = ${rankPosts}")

            rankPosts

        }
    }

    override suspend fun selectInitPosts(limit: Long): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO){
            val snapshot = storeReference
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents
        }
    }

    override suspend fun selectInitPostsWhereFiltering(categories: List<FilteringType>, limit: Long): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO){
            val snapshot = storeReference
                .whereArrayContainsAny("filteringType", categories)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents
        }
    }

    override suspend fun selectNextPosts(lastSnapshot: DocumentSnapshot, limit: Long): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO){
            val snapshot = storeReference
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .startAfter(lastSnapshot)
                .limit(limit)
                .get()
                .await()

            snapshot.documents
        }
    }

    override suspend fun selectNextPostsWhereFiltering(
        lastSnapshot: DocumentSnapshot,
        categories: List<FilteringType>,
        limit: Long
    ): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO){
            val snapshot = storeReference
                .whereArrayContainsAny("filteringType", categories)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .startAfter(lastSnapshot)
                .limit(limit)
                .get()
                .await()

            snapshot.documents
        }
    }

    override suspend fun selectPostFromUser(userId: String): MutableList<PostModel> {
        return withContext(Dispatchers.IO) {
            val snapshot = storeReference
                .whereEqualTo("writer.id", userId)
                .orderBy("writer.id")
                .get()
                .await()

            convertToPostModel(snapshot.documents)
        }
    }

    override suspend fun selectPostFromKey(postKey: String): PostModel {
        return withContext(Dispatchers.IO) {
            val snapshot = storeReference
                .whereEqualTo("key", postKey)
                .get()
                .await()
            var postModel = PostModel()
            if(snapshot.documents.size == 1) {
                val hashMap = snapshot.documents[0].data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
               postModel = gson.fromJson(toJson, PostModel::class.java)
            } else {
                Log.w("PostRepositoryImpl", "result is not a post")
            }
            postModel
        }
    }
    //    override suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<PostModel> {
//        return suspendCancellableCoroutine { continuation ->
//            for (post in posts) {
//                storageReference.child(post.key).listAll().addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val items = task.result.items
//                        if (items.size > 0) {
//                            task.result.items[0].downloadUrl.addOnCompleteListener { uri ->
//                                post.imageUris.add(uri.result.toString())
//                            }
//                        }
//                        return@addOnCompleteListener
//                    } else {
//                        continuation.resumeWithException(Exception("Unknown error"))
//                    }
//                }.addOnFailureListener {
//                    continuation.resumeWithException(it)
//                }
//            }
//            continuation.resume(posts)
//        }
//    }



    override suspend fun selectPostMainImage(post: PostModel): StorageReference? {
        return withContext(Dispatchers.IO) {
            val items = storageReference.child(post.key)
                .listAll()
                .await()
                .items

            if(items.size > 0)
                items[0]
            else
                null
        }
    }
//                    Log.i("PostRepositoryImpl", "success select postMainImage : ${task}")
//                    if (task.isSuccessful) {
//                        val items = task.result.items
//                        if (items.size > 0) {
//                            task.result.items[0].downloadUrl.addOnCompleteListener { uri ->
//                                Log.i(
//                                    "PostRepositoryImpl",
//                                    "success select postMainImage uri : ${uri.result}"
//                                )
//                                post.imageUris.add(uri.result.toString())
//                            }
//                        }

//            Log.d("PostRepositoryImpl", "selectPostMainImage posts = ${posts}")
//        }
//    }

    override suspend fun selectDetailImages(key: String): MutableList<String> {
        return suspendCancellableCoroutine { continuation ->

//            storageReference.child(key).listAll().addOnCompleteListener { task ->
            storageReference.child(key).listAll().addOnCompleteListener { task ->

                if(task.isSuccessful) {
                    val imageUris = mutableListOf<String>()
                    val items = task.result.items

                    for (item in items) {
                        item.downloadUrl.addOnCompleteListener { uri ->
                            Log.i("PostRepositoryImpl", "uri = ${uri}")
                            imageUris.add(uri.result.toString())
                        }.addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                    }
                    continuation.resume(imageUris)
                    return@addOnCompleteListener
                } else {
                    continuation.resumeWithException(Exception("Unknown error"))
                }

            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }

        }
    }

    override suspend fun selectDetailImagesTest(key: String): List<StorageReference> {
        return suspendCancellableCoroutine { continuation ->

//            storageReference.child(key).listAll().addOnCompleteListener { task ->
            storageReference.child(key).listAll().addOnCompleteListener { task ->

                if(task.isSuccessful) {
//                    val imageUris = mutableListOf<String>()
                    val items = task.result.items

//                    for (item in items) {
//                        item.downloadUrl.addOnCompleteListener { uri ->
//                            Log.i("PostRepositoryImpl", "uri = ${uri}")
//                            imageUris.add(uri.result.toString())
//                        }.addOnFailureListener {
//                            continuation.resumeWithException(it)
//                        }
//                    }
                    continuation.resume(items)
                    return@addOnCompleteListener
                } else {
                    continuation.resumeWithException(Exception("Unknown error"))
                }

            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }

        }
    }

    //    override suspend fun selectDetailImages(key: String): MutableList<String> {
//        return coroutineScope { continuation ->
//
//            storageReference.child(key).listAll().addOnCompleteListener { task ->
//
//                if(task.isSuccessful) {
//                    val imageUris = mutableListOf<String>()
//                    val items = task.result.items
//
//                    for (item in items) {
//                        item.downloadUrl.addOnCompleteListener { uri ->
//                            Log.i("PostRepositoryImpl", "uri = ${uri}")
//                            imageUris.add(uri.result.toString())
//                        }.addOnFailureListener {
//                            continuation.resumeWithException(it)
//                        }
//                    }
//                    continuation.resume(imageUris)
//                    return@addOnCompleteListener
//                } else {
//                    continuation.resumeWithException(Exception("Unknown error"))
//                }
//
//            }.addOnFailureListener {
//                continuation.resumeWithException(it)
//            }
//
//        }
//    }

//    override suspend fun selectDetailImages(key: String): List<String> {
//
//        return coroutineScope { task ->
//            launch {
//                val imageUris = mutableListOf<Deferred<String>>()
////                val imageUris = mutableListOf<String>()
//
//                storageReference.child(key).listAll().addOnCompleteListener { task ->
//                    Log.d("PostRepositoryImpl", "success get uri list size = ${task.result.items.size}")
//                    if (task.isSuccessful) {
//                            val items = task.result.items
//                            for (item in items) {
//                                val uri = async { selectDownloadUri(item) }
//                                imageUris.add(uri)
//                        }
//                    }
//                }
//
//                return@launch
//            }
//            imageUris
//        }
//    }

//    override suspend fun selectDetailImages(key: String): MutableList<String> {
//        return CoroutineScope(Dispatchers.Default).async {
//            val imageUris = mutableListOf<String>()
//            storageReference.child(key).listAll().addOnCompleteListener { task ->
//                if(task.isSuccessful) {
//                    launch {
//                        val items = task.result.items
//                        for (item in items) {
//                            val uri = selectDownloadUri(item)
//                            imageUris.add(uri)
//                        }
//                    }
//                }
//            }
//            imageUris
//        }.await()
//    }



//    override suspend fun selectDownloadUri(item: StorageReference): String {
//        return suspendCancellableCoroutine { continuation ->
//            item.downloadUrl.addOnCompleteListener { uri ->
//                Log.i("PostRepositoryImpl", "uri = ${uri.result}")
//                continuation.resume(uri.result.toString())
//                return@addOnCompleteListener
//            }.addOnFailureListener {
//                Log.d("PostRepositoryImpl", "fail downloadUri = ${it}")
//                continuation.resumeWithException(it)
//            }
//        }
//    }

    override suspend fun selectDownloadUri(item: StorageReference): String {
        return withContext(Dispatchers.IO) {
            item.downloadUrl.await().toString()
        }
    }

    override suspend fun updatePost(post: PostModel): Boolean {
        return suspendCancellableCoroutine { continuation ->
//            storeReference.document(post.key).set(post).addOnCompleteListener { task ->
            storeReference.document(post.key).update(
                mapOf(
                    "title" to post.title,
                    "content" to post.content,
                    "filteringTypes" to post.filteringTypes,
                    "updatedDate" to System.currentTimeMillis()
                )
            ).addOnCompleteListener { task ->
//                Log.i("PostRepositoryImpl", "success update question post : ${it}")
                if(task.isSuccessful)
                    continuation.resume(true)
                else
                    continuation.resumeWithException(Exception("Unknown error"))
            }.addOnFailureListener {
//                Log.e("PostRepositoryImpl", "fail update question post : ${it}")
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun deletePost(key: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            storeReference.document(key).delete().addOnCompleteListener { task ->
                Log.i("PostRepositoryImpl", "success delete question post : ${task}")
                if(task.isSuccessful)
                    continuation.resume(true)
                else
                    continuation.resumeWithException(Exception("Unknown error"))
            }.addOnFailureListener {
//                Log.e("PostRepositoryImpl", "fail delete question post : ${it}")
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun deletePostImages(postKey: String) {
        storageReference.child(postKey).listAll().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val items = task.result.items
                Log.i("PostRepositoryImpl", "item size = ${items.size}")
                for (item in items) {
//                            Log.d("GalleryRepositoryImpl", "${item.name}")
//                        item.downloadUrl.addOnCompleteListener{ uri ->
//                            gallery.imageUris.add(uri.result)
//                        }
                    Log.i("PostRepositoryImpl", "item name = ${item.name}")
                    storageReference.child(postKey + "/" + item.name).delete()
                }
            } else {
                task.exception ?: Exception("Unknown error occurred")
            }
            return@addOnCompleteListener
        }.addOnFailureListener {
            Log.d("PostRepositoryImpl", "fail deletePostImage = ${it}")
        }
    }

    override suspend fun addPostViewCount(postKey: String) {
        storeReference.document(postKey).update("viewCount", FieldValue.increment(1)).addOnCompleteListener {
            Log.i("PostRepositoryImpl", "success addPostViewCount = ${it}")
        }.addOnFailureListener {
            Log.d("PostRepositoryImpl", "fail addPostViewCount = ${it}")
        }
    }

    override fun convertToPostModel(documents: List<DocumentSnapshot>): MutableList<PostModel> {
            val allPosts = mutableListOf<PostModel>()
            for (document in documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, PostModel::class.java)
                allPosts.add(fromJson)
            }
        return allPosts
    }
}
