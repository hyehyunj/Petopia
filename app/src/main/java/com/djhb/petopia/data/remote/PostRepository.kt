package com.djhb.petopia.data.remote

import com.djhb.petopia.data.PostModel
import com.google.firebase.storage.StorageReference

interface PostRepository {

    suspend fun createPost(post: PostModel, imageUris: MutableList<String>): Boolean
    suspend fun selectRankPosts(): MutableList<PostModel>
    suspend fun selectPosts(): MutableList<PostModel>
    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<PostModel>
    suspend fun selectPostMainImage(post: PostModel): PostModel
//    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<String>
    suspend fun selectDetailImages(key: String): List<String>
    suspend fun selectDownloadUri(item: StorageReference): String
    suspend fun updatePost(post: PostModel)
    suspend fun deletePost(key: String)

}