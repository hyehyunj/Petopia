package com.djhb.petopia.data.remote

import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel

interface CommentRepository {

    suspend fun selectAllCommentsFromPost(post: PostModel): MutableList<CommentModel>
    suspend fun selectCommentCount(post: PostModel): Int


}