package com.android.petopia.data.remote

import android.util.Log
import com.android.petopia.Table
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.network.FirebaseReference
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MemoryRepositoryImpl: MemoryRepository {

    private val storeReference = Firebase.firestore.collection(Table.MEMORY.tableName)

    override suspend fun createMemory(memory: Memory) {
//        reference.child(memory.key).setValue(memory)
        storeReference.document(memory.key).set(memory).addOnSuccessListener {
            Log.i("MemoryRepositoryImpl", "success create memory : ${it}")
        }.addOnFailureListener {
            Log.e("MemoryRepositoryImpl", "fail create memory : ${it}")
        }
    } //db저장

    override suspend fun selectMemoryList(user: UserModel): MutableList<Memory> {
        return suspendCancellableCoroutine { continuation ->
//            reference.orderByChild("writer/id").equalTo(user.id).get().addOnCompleteListener { task ->
            storeReference.whereEqualTo("writer.id", user.id)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .orderBy("writer.id")
                .get().addOnCompleteListener { task ->
//                Log.i("MemoryRepository", "${task.result.childrenCount}")
                    Log.i("MemoryRepository", "${task.result.documents.size}")
                    val memoryList = mutableListOf<Memory>()

                    if(task.isSuccessful) {
                        val result = task.result
//                    for (child in result.children) {
                        for (document in result.documents) {

                            Log.i("MemoryRepository", "document's key = ${document.id}")

//                        val hashMap = document.value as HashMap<*,*>
                            val hashMap = document.data as HashMap<*,*>
                            val gson = Gson()
                            val toJson = gson.toJson(hashMap)
                            val selectedMemory = gson.fromJson(toJson, Memory::class.java)
                            selectedMemory.key = document.id
                            memoryList.add(selectedMemory)
                        }
                        memoryList.sortByDescending { it.createdDate }
                        continuation.resume(memoryList)
                        return@addOnCompleteListener
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun updateMemory(memory: Memory) {
//        reference.child(memory.key).setValue(memory)
        storeReference.document(memory.key).set(memory).addOnCompleteListener {
            Log.i("MemoryRepositoryImpl", "success update memory : ${it}")
        }.addOnFailureListener {
            Log.i("MemoryRepositoryImpl", "fail update memory : ${it}")
        }
    }

    override suspend fun deleteMemory(memoryKey: String) {
//        reference.child(memoryKey).removeValue()
        storeReference.document(memoryKey).delete().addOnCompleteListener {
            Log.i("MemoryRepositoryImpl", "success delete memory : ${it}")
        }.addOnFailureListener {
            Log.i("MemoryRepositoryImpl", "fail delete memory : ${it}")
        }
    }
}