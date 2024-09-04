package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.Table
import com.djhb.petopia.data.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SignRepositoryImpl: SignRepository {

//    private val reference = FirebaseReference.reference.child(Table.USER.tableName)
    private val reference = Firebase.firestore.collection(Table.USER.tableName)

    override suspend fun createUser(user: UserModel) {
//        reference.child(user.id).setValue(user)
        reference.document(user.id).set(user).addOnSuccessListener {
            Log.i("SignRepositoryImpl", "success signUp = ${it}")
        }.addOnFailureListener {
            Log.e("SignRepositoryImpl", "fail signUp = ${it}")
        }
    }

    override suspend fun selectUser(id: String): UserModel? {
        return suspendCancellableCoroutine { continuation ->
//            reference.orderByChild("id").equalTo(id).get().addOnCompleteListener { task ->
            reference.whereEqualTo("id", id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
//                    if (result.childrenCount.toInt() == 0 || result.childrenCount > 1) {
                    if (result.documents.size == 0 || result.documents.size > 1) {
                        continuation.resume(null)
                        return@addOnCompleteListener
                    }
                    for (document in result.documents) {
                        val hashMap = document.data as HashMap<*, *>
                        val gson = Gson()
                        val toJson = gson.toJson(hashMap)
                        val selectedUser = gson.fromJson(toJson, UserModel::class.java)
                        continuation.resume(selectedUser)
                        return@addOnCompleteListener
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                }
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend  fun updateUser(user: UserModel) {
//        reference.child(user.id).setValue(user)
        reference.document(user.id).set(user).addOnCompleteListener {
            Log.i("SignRepositoryImpl", "success update user = ${it}")
        }.addOnFailureListener {
            Log.e("SignRepositoryImpl", "fail update user = ${it}")
        }
    }

    override suspend fun deleteUser(id: String) {
        reference.document(id).delete().addOnCompleteListener {
            Log.i("SignRepositoryImpl", "success delete user = ${it}")
        }.addOnFailureListener {
            Log.e("SignRepositoryImpl", "fail delete user = ${it}")
        }
    }
}