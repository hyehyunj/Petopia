package com.djhb.petopia.data.remote

import com.djhb.petopia.data.PostModel

interface PostRepository {

    suspend fun createPost(post: PostModel, imageUris: MutableList<String>): Boolean
    suspend fun selectRankPosts(): MutableList<PostModel>
    suspend fun selectPosts(): MutableList<PostModel>
    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<PostModel>
    suspend fun selectPostMainImage(post: PostModel): PostModel
//    suspend fun selectPostMainImage(posts: MutableList<PostModel>): MutableList<String>
    suspend fun updatePost(post: PostModel)
    suspend fun deletePost(key: String)

}