package com.android.petopia.data.remote

import com.android.petopia.data.CommentModel
import com.android.petopia.data.PostModel

interface CommentRepository {

    suspend fun selectAllCommentsFromPost(post: PostModel): MutableList<CommentModel>
    suspend fun selectCommentCount(post: PostModel): Int


}