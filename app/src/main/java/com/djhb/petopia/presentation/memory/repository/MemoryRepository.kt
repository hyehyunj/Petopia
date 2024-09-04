package com.djhb.petopia.presentation.memory.repository

import com.djhb.petopia.data.Memory

interface MemoryRepository {
    fun getMemoryList(): List<Memory>
    fun addMemoryList(memory: Memory)
    fun deleteMemoryList(memory: Memory)
}