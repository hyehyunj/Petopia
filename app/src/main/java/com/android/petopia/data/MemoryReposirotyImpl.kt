package com.android.petopia.data

import com.android.petopia.presentation.memory.repository.MemoryRepository

class MemoryReposirotyImpl(private val memoryModel: List<MemoryModel>) : MemoryRepository {
    override fun getMemoryList(): List<MemoryModel> {
        return memoryModel
    }

    override fun addMemoryList(memory: MemoryModel) {
        TODO("Not yet implemented")
    }

    override fun deleteMemoryList(memory: MemoryModel) {
        TODO("Not yet implemented")
    }
}