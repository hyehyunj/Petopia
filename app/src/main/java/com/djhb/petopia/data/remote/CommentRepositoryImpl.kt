package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.Table
import com.djhb.petopia.data.CommentModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CommentRepositoryImpl(val table: Table): CommentRepository {

    //    private val reference = Firebase.firestore.collection(Table.QUESTION_COMMENT.tableName)
    private val reference = Firebase.firestore.collection(table.tableName)

    override suspend fun createComment(comment: CommentModel): CommentModel {

        return suspendCancellableCoroutine { continuation ->
            reference.document(comment.key).set(comment).addOnCompleteListener { task ->
                if(task.isSuccessful) {
//                    Log.i("CommentRepositoryImpl", "success create comment = ${task}")
                    continuation.resume(comment)
                    return@addOnCompleteListener
                } else {
                    continuation.resumeWithException(Exception(task.exception))
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
                .orderBy("createdDate", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener { task ->
                    val comments = mutableListOf<CommentModel>()
                    if(task.isSuccessful) {
                        val documents = task.result.documents

                        continuation.resume(convertToCommentModels(documents))
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(Exception(task.exception))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun selectCommentCount(postKey: String): Long {
        return withContext(Dispatchers.IO) {

            val snapshot = reference.whereEqualTo("postKey", postKey)
                .count()
                .get(AggregateSource.SERVER)
                .await()
            snapshot.count
        }
    }

    override suspend fun updateComment(comment: CommentModel) {
        reference.document(comment.key).set(comment)
    }

    override suspend fun deleteComment(commentKey: String) {
        reference.document(commentKey).delete()
    }

//    override suspend fun deleteAllCommentFromUser(userId: String) {
//        reference.
//    }

    override suspend fun selectAllCommentsFromUser(userId: String): MutableList<CommentModel> {
        return withContext(Dispatchers.IO) {
            val snapshot = reference
                .whereEqualTo("writer.id", userId)
                .get()
                .await()
            convertToCommentModels(snapshot.documents)
        }
    }

    override fun convertToCommentModels(documents: List<DocumentSnapshot>): MutableList<CommentModel> {

        val comments = mutableListOf<CommentModel>()

        for (document in documents) {
            val hashMap = document.data as HashMap<*, *>
            val gson = Gson()
            val toJson = gson.toJson(hashMap)
            val fromJson = gson.fromJson(toJson, CommentModel::class.java)
            comments.add(fromJson)
        }
        return comments
    }
}