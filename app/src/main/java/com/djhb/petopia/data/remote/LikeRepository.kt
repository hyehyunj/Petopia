package com.djhb.petopia.data.remote

import com.djhb.petopia.data.LikeModel
import com.google.firebase.firestore.DocumentSnapshot

interface LikeRepository {

    //    suspend fun selectLikeFromPost(post: PostModel, user: UserModel): CommentModel

    suspend fun selectLikeList(postKey: String): MutableList<LikeModel>
    suspend fun selectLikeFromUser(postKey: String, userId: String): LikeModel
    suspend fun createLike(like: LikeModel)
    suspend fun deleteLike(likeKey: String)
    fun convertToLikeModel(snapshot: DocumentSnapshot): LikeModel


}