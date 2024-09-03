package com.android.petopia.presentation.letter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.petopia.data.Memory
import com.android.petopia.data.UserModel
import kotlinx.coroutines.launch

class LetterViewModel(private val letterRepository: LetterRepository) : ViewModel() {
    private val _letterListLiveData = MutableLiveData<List<Memory>>()
    val letterListLiveData: LiveData<List<Memory>> = _letterListLiveData

//    fun addLetterList(memory: Memory) {
//        viewModelScope.launch {
//            letterRepository.createLetter(memory)
//            loadLetterList(memory.writer)
//        }
//    }
//
//    fun loadLetterList(user: UserModel) {
//        viewModelScope.launch {
//            val memoryList = letterRepository.selectLetterList(user)
//            _letterListLiveData.value = memoryList
//        }
//    }
//
//    fun updateLetterList(memory: Memory) {
//        viewModelScope.launch {
//            letterRepository.updateLetter(memory)
//            loadLetterList(memory.writer)
//        }
//    }
//
//    fun deleteLetterList(memory: Memory) {
//        viewModelScope.launch {
//            letterRepository.deleteLetter(memory.key)
//            loadLetterList(memory.writer)
//        }
//    }

    class LetterViewModelFactory(private val letterRepository: LetterRepositoryImpl) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LetterViewModel::class.java)) {
                return LetterViewModel(letterRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }


    }


}