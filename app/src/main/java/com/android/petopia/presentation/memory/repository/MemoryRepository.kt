package com.android.petopia.presentation.memory.repository

import android.content.Context
import com.android.petopia.data.MemoryModel

interface MemoryRepository {
    fun getMemoryList(): List<MemoryModel>
    fun addMemoryList(memory: MemoryModel)
    fun deleteMemoryList(memory: MemoryModel)
}