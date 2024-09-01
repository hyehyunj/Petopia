package com.android.petopia.presentation.memory.repository

import android.content.Context
import com.android.petopia.data.Memory

interface MemoryRepository {
    fun getMemoryList(): List<Memory>
    fun addMemoryList(memory: Memory)
    fun deleteMemoryList(memory: Memory)
}