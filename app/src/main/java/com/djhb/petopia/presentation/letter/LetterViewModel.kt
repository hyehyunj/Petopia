package com.djhb.petopia.presentation.letter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.LetterRepository
import kotlinx.coroutines.launch

class LetterViewModel(private val letterRepository: LetterRepository) : ViewModel() {
    private val _letterListLiveData = MutableLiveData<List<LetterModel>>()
    val letterListLiveData: LiveData<List<LetterModel>> = _letterListLiveData

    private val _selectedLetter = MutableLiveData<LetterModel>()
    val selectedLetter: LiveData<LetterModel> = _selectedLetter

    private val _isLetterSaved = MutableLiveData<Boolean>()
    val isLetterSaved: LiveData<Boolean> = _isLetterSaved

    fun setSelectedLetter(letterModel: LetterModel) {
        _selectedLetter.value = letterModel
    }

    fun addLetterList(letterModel: LetterModel) {
        viewModelScope.launch {
            letterRepository.createLetter(letterModel)
            loadLetterList(letterModel.writer)
        }
    }

    fun loadLetterList(user: UserModel) {
        viewModelScope.launch {
            val memoryList = letterRepository.selectLetterList(user)
            _letterListLiveData.value = memoryList
        }
    }

    fun setLetterSaved(isSaved: Boolean) {
        _isLetterSaved.value = isSaved
    }

    fun updateLetterList(letterModel: LetterModel) {
        viewModelScope.launch {
            val currentLetter = _letterListLiveData.value?.toMutableList() ?: mutableListOf()
            val index = currentLetter.indexOfFirst { it.key == letterModel.key }
            if (index != -1) {
                currentLetter[index] = letterModel
                _letterListLiveData.value = currentLetter
            }
            letterRepository.updateLetter(letterModel)
            loadLetterList(letterModel.writer)
        }
    }

    fun deleteLetterList(letterModel: LetterModel) {
        viewModelScope.launch {
            letterRepository.deleteLetter(letterModel.key)
            loadLetterList(letterModel.writer)
        }
    }

    class LetterViewModelFactory(private val letterRepository: LetterRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LetterViewModel::class.java)) {
                return LetterViewModel(letterRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }


    }


}