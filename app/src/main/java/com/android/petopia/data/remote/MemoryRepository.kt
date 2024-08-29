package com.android.petopia.data.remote

import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel

interface MemoryRepository {

    suspend fun createMemory(memory: Memory)
    suspend fun selectMemoryList(user: UserModel): MutableList<Memory>
    suspend fun updateMemory(memory: Memory)
    suspend fun deleteMemory(memoryIndex: Int)

}