package com.android.petopia.data

import com.android.petopia.presentation.memory.repository.MemoryRepository

class MemoryReposirotyImpl(private val memoryModel: MutableList<Memory>) : MemoryRepository {
    override fun getMemoryList(): List<Memory> {
        return memoryModel
    }

    override fun addMemoryList(memory: Memory) {
        memoryModel.add(memory)
    }

    override fun deleteMemoryList(memory: Memory) {
        memoryModel.remove(memory)
    }


}