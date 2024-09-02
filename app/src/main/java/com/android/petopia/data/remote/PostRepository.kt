package com.android.petopia.data.remote

import com.android.petopia.data.PostModel
import com.android.petopia.data.UserModel

interface PostRepository {

    suspend fun selectRankPosts(): MutableList<PostModel>
    suspend fun selectPosts(): MutableList<PostModel>

}