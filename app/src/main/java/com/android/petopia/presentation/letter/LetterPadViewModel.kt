package com.android.petopia.presentation.letter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LetterPadViewModel : ViewModel() {
    private val _selectBackgroundResId = MutableLiveData<Int?>()
    val selectBackgroundResId: MutableLiveData<Int?> = _selectBackgroundResId

    fun selectBackground(resId: Int) {
        _selectBackgroundResId.value = resId
    }


}