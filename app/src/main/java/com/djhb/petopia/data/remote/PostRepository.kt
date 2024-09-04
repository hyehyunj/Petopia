package com.djhb.petopia.data.remote

import com.djhb.petopia.data.PostModel

interface PostRepository {

    suspend fun selectRankPosts(): MutableList<PostModel>
    suspend fun selectPosts(): MutableList<PostModel>

}