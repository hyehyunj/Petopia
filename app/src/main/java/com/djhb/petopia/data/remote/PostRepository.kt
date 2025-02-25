package com.djhb.petopia.data.remote

import com.djhb.petopia.FilteringType
import com.djhb.petopia.data.PostModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference

interface PostRepository {

//    suspend fun createPost(post: PostModel, imageUris: MutableList<String>): PostModel
    suspend fun createPost(post: PostModel, imageUris: MutableList<String>)
    suspend fun createPostImages(post: PostModel, imageUris: MutableList<String>): Boolean
    suspend fun selectRankPosts(): MutableList<PostModel>
    suspend fun selectRankPostsWhereFiltering(categories: List<FilteringType>): MutableList<PostModel>
//    suspend fun selectPosts(): MutableList<PostModel>
    suspend fun selectInitPosts(limit: Long = 10): List<DocumentSnapshot>
    suspend fun selectInitPostsWhereFiltering(categories: List<FilteringType>, limit: Long = 10): List<DocumentSnapshot>

    suspend fun selectNextPosts(lastSnapshot: DocumentSnapshot, limit: Long = 10): List<DocumentSnapshot>
    suspend fun selectNextPostsWhereFiltering(lastSnapshot: DocumentSnapshot, categories: List<FilteringType>, limit: Long = 10): List<DocumentSnapshot>
    suspend fun selectPostFromUser(userId: String): MutableList<PostModel>
    suspend fun selectPostFromKey(postKey: String): PostModel
//    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<PostModel>
    suspend fun selectPostMainImage(post: PostModel): StorageReference?
//    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<String>
    suspend fun selectDetailImages(key: String): List<String>
    suspend fun selectDetailImagesTest(key: String): List<StorageReference>
    suspend fun selectDownloadUri(item: StorageReference): String
    suspend fun updatePost(post: PostModel): Boolean
    suspend fun deletePost(key: String): Boolean
    suspend fun deletePostImages(postKey: String)
    suspend fun addPostViewCount(postKey: String)
    fun convertToPostModel(documents: List<DocumentSnapshot>): MutableList<PostModel>

}