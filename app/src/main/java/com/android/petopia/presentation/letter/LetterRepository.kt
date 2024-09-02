package com.android.petopia.presentation.letter

import com.android.petopia.data.Memory

interface LetterRepository {
    fun getMemoryList(): List<Memory>
    fun addMemoryList(memory: Memory)
    fun deleteMemoryList(memory: Memory)
}