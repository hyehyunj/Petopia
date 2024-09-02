package com.android.petopia.data.remote

import com.android.petopia.data.CommentModel
import com.android.petopia.data.PostModel
import com.android.petopia.data.UserModel

interface LikeRepository {

    suspend fun selectLikeCount(post: PostModel): Int
    suspend fun selectLikeFromPost(post: PostModel, user: UserModel): CommentModel
}