package com.djhb.petopia.data.remote

import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel
import com.google.firebase.firestore.DocumentSnapshot

interface CommentRepository {

    suspend fun createComment(comment: CommentModel): CommentModel
    suspend fun selectAllCommentsFromPost(postKey: String): MutableList<CommentModel>
    suspend fun selectAllCommentsFromUser(userId: String): MutableList<CommentModel>
    suspend fun selectCommentCount(postKey: String): Long
//    suspend fun selectCommentCount(post: PostModel): Int
    suspend fun updateComment(comment: CommentModel)
    suspend fun deleteComment(commentKey: String)
//    suspend fun deleteAllCommentFromUser(userId: String)
    fun convertToCommentModels(documents: List<DocumentSnapshot>): MutableList<CommentModel>

}