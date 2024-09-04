package com.djhb.petopia.data.remote

import com.djhb.petopia.data.Memory
import com.djhb.petopia.data.UserModel

interface MemoryRepository {

    suspend fun createMemory(memory: Memory)
    suspend fun selectMemoryList(user: UserModel): MutableList<Memory>
    suspend fun updateMemory(memory: Memory)
    suspend fun deleteMemory(memoryKey: String)

}