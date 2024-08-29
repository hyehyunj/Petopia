package com.android.petopia.presentation.memory.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.data.MemoryModel
import com.android.petopia.presentation.memory.repository.MemoryRepository

class MemoryViewModel(private val memoryRepository: MemoryRepository) : ViewModel() {

    private val _memoryListLiveData = MutableLiveData<List<MemoryModel>>()
    val memoryListLiveData: LiveData<List<MemoryModel>> = _memoryListLiveData

    private val memoryItems = mutableListOf<MemoryModel>()



    fun addMemoryList(memory: MemoryModel) {
        val newMemory = MemoryModel(memory.memoryTitle, memory.memoryDate, memory.memoryContent)
        memoryItems.add(newMemory)
        _memoryListLiveData.value = memoryItems.toList()
    }

//    fun deleteMemoryList(memory: MemoryModel) {
//        memoryRepository.deleteMemoryList(memory)
//        loadMemoryList()
//    }

//    class MemoryViewModelFactory(private val memoryRepository: MemoryRepository) :
//        ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MemoryViewModel::class.java)) {
//                return MemoryViewModel(memoryRepository) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//
//
//    }
}