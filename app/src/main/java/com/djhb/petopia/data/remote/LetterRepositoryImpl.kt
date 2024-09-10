package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.Table
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.UserModel
import com.google.firebase.Firebase
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

class LetterRepositoryImpl: LetterRepository {

    private val reference = Firebase.firestore.collection(Table.LETTER.tableName)

    override suspend fun createLetter(letterModel: LetterModel) {
        reference.document(letterModel.key).set(letterModel).addOnCompleteListener {
            Log.i("LetterRepositoryImpl", "success create letter : ${it}")
        }.addOnFailureListener {
            Log.e("LetterRepositoryImpl", "fail create letter : ${it}")
        }
    }

    override suspend fun selectInitLetterList(user: UserModel): List<DocumentSnapshot> {
        return withContext(Dispatchers.IO) {
            val snapshot = reference
                .whereEqualTo("writer.id", user.id)
                .orderBy("writer.id")
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            snapshot.documents
        }
    }

//    override suspend fun selectLetterList(user: UserModel, snapshot: DocumentSnapshot): List<DocumentSnapshot> {
//        return withContext(Dispatchers.IO) {
//            val resultSnapshot = reference
//                .whereEqualTo("writer.id", user.id)
//                .orderBy("writer.id")
//                .orderBy("createdDate", Query.Direction.DESCENDING)
//                .startAfter(snapshot)
//                .limit(10)
//                .get()
//                .await()
//
//            resultSnapshot.documents
//        }
//    }

    override suspend fun selectLetterList(user: UserModel): MutableList<LetterModel> {
        return suspendCancellableCoroutine { continuation ->
            reference
                .whereEqualTo("writer.id", user.id)
                .orderBy("writer.id")
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    val letters = mutableListOf<LetterModel>()
                    if(task.isSuccessful) {
                        val documents = task.result.documents
                        for (document in documents) {
                            val hashMap = document.data as HashMap<*, *>
                            val gson = Gson()
                            val toJson = gson.toJson(hashMap)
                            val fromJson = gson.fromJson(toJson, LetterModel::class.java)
                            letters.add(fromJson)
                        }
                        continuation.resume(letters)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }
//
//    override suspend fun selectLetterList(user: UserModel, snapshot: DocumentSnapshot): MutableList<LetterModel> {
//        return suspendCancellableCoroutine { continuation ->
//            reference
//                .whereEqualTo("writer.id", user.id)
//                .startAfter(snapshot)
//                .orderBy("writer.id")
//                .orderBy("createdDate", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener { task ->
//                    val letters = mutableListOf<LetterModel>()
//                    if(task.isSuccessful) {
//                        val documents = task.result.documents
//                        for (document in documents) {
//                            val hashMap = document.data as HashMap<*, *>
//                            val gson = Gson()
//                            val toJson = gson.toJson(hashMap)
//                            val fromJson = gson.fromJson(toJson, LetterModel::class.java)
//                            letters.add(fromJson)
//                        }
//                        continuation.resume(letters)
//                        return@addOnCompleteListener
//                    } else {
//                        continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
//                    }
//                }.addOnFailureListener {
//                    continuation.resumeWithException(it)
//                }
//        }
//    }

    override suspend fun updateLetter(letterModel: LetterModel) {
        reference.document(letterModel.key).set(letterModel).addOnCompleteListener {
            Log.i("LetterRepositoryImpl", "success update letter : ${it}")
        }.addOnFailureListener {
            Log.e("LetterRepositoryImpl", "fail update letter : ${it}")
        }
    }

    override suspend fun deleteLetter(key: String) {
        reference.document(key).delete().addOnCompleteListener {
            Log.i("LetterRepositoryImpl", "success delete letter : ${it}")
        }.addOnFailureListener {
            Log.i("LetterRepositoryImpl", "fail delete letter : ${it}")
        }
    }


    override fun convertToLetterModel(documents: List<DocumentSnapshot>): MutableList<LetterModel> {
        val letters = mutableListOf<LetterModel>()
            for (document in documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, LetterModel::class.java)
                letters.add(fromJson)
            }
        return letters
    }
}