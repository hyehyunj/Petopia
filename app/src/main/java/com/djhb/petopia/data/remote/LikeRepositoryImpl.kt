package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.Table
import com.djhb.petopia.data.LikeModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LikeRepositoryImpl(val table: Table): LikeRepository {

//    private val reference = Firebase.firestore.collection(Table.QUESTION_LIKE.tableName)
    private val reference = Firebase.firestore.collection(table.tableName)

    override suspend fun selectLikeList(postKey: String): MutableList<LikeModel> {
        return withContext(Dispatchers.IO) {
            val snapshot = reference
                .whereEqualTo("postKey", postKey)
                .get()
                .await()

            val likes = mutableListOf<LikeModel>()
            val documents = snapshot.documents
            for (document in documents) {
                likes.add(convertToLikeModel(document))
            }

            likes
        }
    }

    override suspend fun selectLikeCount(postKey: String): Long {
        return withContext(Dispatchers.IO) {
            val snapshot = reference.whereEqualTo("postKey", postKey)
                .count()
                .get(AggregateSource.SERVER)
                .await()

            snapshot.count
        }
    }

    override suspend fun selectLikeFromUser(postKey: String, userId: String): LikeModel {
        return withContext(Dispatchers.IO){
            val snapshot = reference.whereEqualTo("postKey", postKey)
                .whereEqualTo("userId", userId)
                .orderBy("postKey")
                .orderBy("userId")
                .get()
                .await()
            val documents = snapshot.documents
            if(documents.size == 1)
                convertToLikeModel(snapshot.documents[0])
            else
                LikeModel()
        }
    }


    override suspend fun createLike(like: LikeModel) {
        withContext(Dispatchers.IO){
            reference
                .document(like.key)
                .set(like)
                .addOnCompleteListener {
                    Log.i("LikeRepositoryImpl", "success create like, userId = ${like.userId}")
                }.addOnFailureListener {
                    Log.d("LikeRepositoryImpl", "fail create like ${it}")
                }
        }
    }

    override suspend fun deleteLike(likeKey: String) {
        withContext(Dispatchers.IO){
            reference
                .document(likeKey)
                .delete()
                .await()
//                .addOnCompleteListener {
//                    Log.i("LikeRepositoryImpl", "success delete like, postKey = ${likeKey}")
//                }.addOnFailureListener {
//                    Log.d("LikeRepositoryImpl", "fail delete like ${it}")
//                }
        }
    }

    override fun convertToLikeModel(snapshot: DocumentSnapshot): LikeModel {
        val hashMap = snapshot.data as HashMap<*, *>
        val gson = Gson()
        val toJson = gson.toJson(hashMap)
        val fromJson = gson.fromJson(toJson, LikeModel::class.java)
        return fromJson
    }
}