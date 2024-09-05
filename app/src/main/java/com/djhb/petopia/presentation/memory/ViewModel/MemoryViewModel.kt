package com.djhb.petopia.presentation.memory.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.Memory
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.MemoryRepositoryImpl
import kotlinx.coroutines.launch

class MemoryViewModel(private val memoryRepository: MemoryRepositoryImpl) : ViewModel() {

    private val _memoryListLiveData = MutableLiveData<List<Memory>>()
    val memoryListLiveData: LiveData<List<Memory>> = _memoryListLiveData

    private val memoryItems = mutableListOf<Memory>()

    // 메모리 제목
    private val _memoryTitle = MutableLiveData<String>()
    val memoryTitle: LiveData<String> = _memoryTitle

    // 메모리 작성 날짜
    private val _memoryDate = MutableLiveData<String>()
    val memoryDate: LiveData<String> = _memoryDate

    private val _isMemorySaved = MutableLiveData<Boolean>()
    val isMemorySaved: LiveData<Boolean> = _isMemorySaved

    private val _selectedMemory = MutableLiveData<Memory>()
    val selectedMemory: LiveData<Memory> = _selectedMemory

    private val _isModify = MutableLiveData<Boolean>()
    val isModify: LiveData<Boolean> = _isModify

    fun setMemoryDate(date: String) {
        _memoryDate.value = date
    }

    fun setMemoryTitle(title: String) {
        _memoryTitle.value = title
    }

    fun setMemorySaved(isSaved: Boolean) {
        _isMemorySaved.value = isSaved
    }

    fun setSelectedMemory(memory: Memory) {
        _selectedMemory.value = memory
    }

    fun setModify(isModify: Boolean) {
        _isModify.value = isModify
    }

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
            val currentMemory = _memoryListLiveData.value?.toMutableList() ?: mutableListOf()
            val index = currentMemory.indexOfFirst { it.key == memory.key }
            if (index != -1) {
                currentMemory[index] = memory
                _memoryListLiveData.value = currentMemory
            }
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