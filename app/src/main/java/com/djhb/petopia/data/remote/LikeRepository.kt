package com.djhb.petopia.data.remote

import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.UserModel

interface LikeRepository {

    suspend fun selectLikeCount(post: PostModel): Int
    suspend fun selectLikeFromPost(post: PostModel, user: UserModel): CommentModel
}