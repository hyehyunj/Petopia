package com.android.petopia.presentation.memory.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.MemoryRepositoryImpl
import kotlinx.coroutines.launch

class MemoryViewModel(private val memoryRepository: MemoryRepositoryImpl) : ViewModel() {

    private val _memoryListLiveData = MutableLiveData<List<Memory>>()
    val memoryListLiveData: LiveData<List<Memory>> = _memoryListLiveData

    private val memoryItems = mutableListOf<Memory>()


    fun addMemoryList(memory: Memory) {
        viewModelScope.launch {
            memoryRepository.createMemory(memory)
            loadMemoryList(memory.writer)
        }
    }

    fun loadMemoryList(user: UserModel) {
        viewModelScope.launch {
            val memoryList = memoryRepository.selectMemoryList(user)
            _memoryListLiveData.value = memoryList
        }
    }

    fun updateMemoryList(memory: Memory) {
        viewModelScope.launch {
            memoryRepository.updateMemory(memory)
            loadMemoryList(memory.writer)
        }
    }

    fun deleteMemoryList(memory: Memory) {
        viewModelScope.launch {
            memoryRepository.deleteMemory(memory.key)
            loadMemoryList(memory.writer)
        }
    }


    class MemoryViewModelFactory(private val memoryRepository: MemoryRepositoryImpl) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MemoryViewModel::class.java)) {
                return MemoryViewModel(memoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }


    }
}