package com.android.petopia.data.remote

import com.android.petopia.Table
import com.android.petopia.network.FirebaseReference
import com.android.petopia.data.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SignRepositoryImpl: SignRepository {

    private val reference = FirebaseReference.reference.child(Table.USER.tableName)

    override suspend fun createUser(user: UserModel) {
        reference.child(user.id).setValue(user)
    }

    override suspend fun selectUser(id: String): UserModel? {
        return suspendCancellableCoroutine { continuation ->
            reference.orderByChild("id").equalTo(id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result.childrenCount.toInt() == 0 || result.childrenCount > 1) {
                        continuation.resume(null)
                        return@addOnCompleteListener
                    }
                    for (child in result.children) {
                        val hashMap = child.value as HashMap<*, *>
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
        reference.child(user.id).setValue(user)
    }
}