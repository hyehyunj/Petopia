package com.android.petopia.presentation.letter

import com.android.petopia.data.Memory

class LetterRepositoryImpl(private val letterModel: MutableList<Memory>) : LetterRepository {
    override fun getMemoryList(): List<Memory> {
        return letterModel
    }

    override fun addMemoryList(memory: Memory) {
        letterModel.add(memory)
    }

    override fun deleteMemoryList(memory: Memory) {
        letterModel.remove(memory)
    }
}