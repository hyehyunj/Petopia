package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.data.CommentModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CommentRepositoryImpl: CommentRepository {

    private val reference = Firebase.firestore.collection("questionComment")

    override suspend fun createComment(comment: CommentModel): CommentModel {

        return suspendCancellableCoroutine { continuation ->
            reference.document(comment.key).set(comment).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.i("CommentRepositoryImpl", "success create comment = ${task}")
                    continuation.resume(comment)
                    return@addOnCompleteListener
                } else {
                    continuation.resumeWithException(Exception("Unknown error"))
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun selectAllCommentsFromPost(postKey: String): MutableList<CommentModel> {
        return suspendCancellableCoroutine { continuation ->

            reference
                .whereEqualTo("postKey", postKey)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    val comments = mutableListOf<CommentModel>()
                    if(task.isSuccessful) {
                        val documents = task.result.documents
                        for (document in documents) {
                            val hashMap = document.data as HashMap<*, *>
                            val gson = Gson()
                            val toJson = gson.toJson(hashMap)
                            val fromJson = gson.fromJson(toJson, CommentModel::class.java)
                            comments.add(fromJson)
                        }
                        continuation.resume(comments)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception("Unknown error"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun updateComment(comment: CommentModel) {
        reference.document(comment.key).set(comment)
    }

    override suspend fun deleteComment(commentKey: String) {
        reference.document(commentKey).delete()
    }
}