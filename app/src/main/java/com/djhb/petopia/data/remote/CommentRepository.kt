package com.djhb.petopia.data.remote

import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel

interface CommentRepository {

    suspend fun createComment(comment: CommentModel): CommentModel
    suspend fun selectAllCommentsFromPost(postKey: String): MutableList<CommentModel>
//    suspend fun selectCommentCount(post: PostModel): Int
    suspend fun updateComment(comment: CommentModel)
    suspend fun deleteComment(commentKey: String)


}