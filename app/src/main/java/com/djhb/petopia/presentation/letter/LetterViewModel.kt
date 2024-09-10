package com.djhb.petopia.presentation.letter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.LetterRepository
import com.djhb.petopia.data.remote.LetterRepositoryImpl
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

class LetterViewModel(private val letterRepository: LetterRepositoryImpl) : ViewModel() {
    private val _letterListLiveData = MutableLiveData<List<LetterModel>>()
    val letterListLiveData: LiveData<List<LetterModel>> = _letterListLiveData

    private val _selectedLetter = MutableLiveData<LetterModel>()
    val selectedLetter: LiveData<LetterModel> = _selectedLetter

    private val _isLetterSaved = MutableLiveData<Boolean>()
    val isLetterSaved: LiveData<Boolean> = _isLetterSaved

    private val _selectBackgroundResId = MutableLiveData<Int?>()
    val selectBackgroundResId: MutableLiveData<Int?> = _selectBackgroundResId

//    무한스크롤관련
//    private lateinit var lastSnapshot: DocumentSnapshot

    //무한스크롤 관련
//    private var letterListResult = mutableListOf<LetterModel>()

    fun selectBackground(resId: Int) {
        _selectBackgroundResId.value = resId
    }

    fun setSelectedLetter(letterModel: LetterModel) {
        _selectedLetter.value = letterModel
    }

    fun addLetterList(letterModel: LetterModel) {
        viewModelScope.launch {
            letterRepository.createLetter(letterModel)
            loadLetterList(letterModel.writer)
        }
    }

//    fun loadInitLetterList(user: UserModel) {
//        viewModelScope.launch {
//            val documents = letterRepository.selectInitLetterList(user)
//            if (documents.size > 0) {
//                lastSnapshot = documents[documents.size - 1]
////            val memoryList = letterRepository.convertToLetterModel(documents)
//                letterListResult = letterRepository.convertToLetterModel(documents)
//                _letterListLiveData.value = letterListResult
//            }
//        }
//    }

    fun loadLetterList(user: UserModel) {
        viewModelScope.launch {
            val letterList = letterRepository.selectLetterList(user)
            _letterListLiveData.value = letterList
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