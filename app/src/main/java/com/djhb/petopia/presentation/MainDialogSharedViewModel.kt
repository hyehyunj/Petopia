package com.djhb.petopia.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//메인 액티비티와 다이얼로그간 공유뷰모델
class MainDialogSharedViewModel : ViewModel() {

    //종류 : "CANCEL" 강아지 , "REMOVE" 고양이
    private val _dialogModeLiveData = MutableLiveData("CANCEL")
    val dialogModeLiveData: LiveData<String> = _dialogModeLiveData


    fun updateDialogMode(mode: String) {
        _dialogModeLiveData.value = mode
    }
    }
