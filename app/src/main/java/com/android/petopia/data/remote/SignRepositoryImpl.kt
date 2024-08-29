package com.android.petopia.data.remote

import android.util.Log
import com.android.petopia.Table
import com.android.petopia.network.FirebaseReference
import com.android.petopia.presentation.community.User
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SignRepositoryImpl: SignRepository {

    private val reference = FirebaseReference.reference.child(Table.USER.tableName)

    override suspend fun createUser(user: User) {
        reference.child(user.id).setValue(user)
    }

//    override suspend fun selectUser(id: String): User? {
//
//        var selectedUser: User? = null
//
//        reference.orderByChild("id").equalTo(id).get().addOnCompleteListener {
//
//            val result = it.result
//            Log.i("SignRepositoryImpl", "select user = ${result}")
//            Log.i("SignRepositoryImpl", "childrenCount = ${result.childrenCount}")
//
//            if(result.childrenCount.toInt() == 0 || result.childrenCount > 1) {
//                return@addOnCompleteListener
//            }
//
//            for (child in result.children) {
//                val hashMap = child.value as HashMap<*, *>
//                val gson = Gson()
//                val toJson = gson.toJson(hashMap)
//                selectedUser = gson.fromJson(toJson, User::class.java)
//            }
//            Log.i("SignRepositoryImpl", "find select user = ${selectedUser}")
//        }.addOnFailureListener{
//            Log.e("CommunityFragment", "Error getting data", it)
//        }
//
//        Log.i("SignRepositoryImpl", "return select user = ${selectedUser}")
//        return selectedUser
//    }

    override suspend fun selectUser(id: String): User? {
        return suspendCancellableCoroutine { continuation ->
            reference.orderByChild("id").equalTo(id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    Log.i("SignRepositoryImpl", "select user = ${result}")
                    Log.i("SignRepositoryImpl", "childrenCount = ${result.childrenCount}")
                    if (result.childrenCount.toInt() == 0 || result.childrenCount > 1) {
                        continuation.resume(null)
                        return@addOnCompleteListener
                    }
                    for (child in result.children) {
                        val hashMap = child.value as HashMap<*, *>
                        val gson = Gson()
                        val toJson = gson.toJson(hashMap)
                        val selectedUser = gson.fromJson(toJson, User::class.java)
                        Log.i("SignRepositoryImpl", "find select user = ${selectedUser}")
                        continuation.resume(selectedUser)
                        return@addOnCompleteListener
                    }
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                }
            }.addOnFailureListener {
                Log.e("CommunityFragment", "Error getting data", it)
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend  fun updateUser(user: User) {

    }
}