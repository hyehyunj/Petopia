package com.android.petopia.data.remote

import android.util.Log
import com.android.petopia.Table
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.network.FirebaseReference
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MemoryRepositoryImpl: MemoryRepository {

    private val reference = FirebaseReference.reference.child(Table.MEMORY.tableName)

    override suspend fun createMemory(memory: Memory) {
        reference.child(memory.key).setValue(memory)
    }

    override suspend fun selectMemoryList(user: UserModel): MutableList<Memory> {
        return suspendCancellableCoroutine { continuation ->
            reference.orderByChild("writer/id").equalTo(user.id).get().addOnCompleteListener { task ->
                Log.i("MemoryRepository", "${task.result.childrenCount}")
                val memoryList = mutableListOf<Memory>()

                if(task.isSuccessful) {
                    val result = task.result

                    for (child in result.children) {

                        Log.i("MemoryRepository", "child's key = ${child.key}")

                        val hashMap = child.value as HashMap<*,*>
                        val gson = Gson()
                        val toJson = gson.toJson(hashMap)
                        val selectedMemory = gson.fromJson(toJson, Memory::class.java)
                        selectedMemory.key = child.key.toString()
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
        reference.child(memory.key).setValue(memory)
    }

    override suspend fun deleteMemory(memoryKey: String) {
        reference.child(memoryKey).removeValue()
    }
}