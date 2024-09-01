package com.android.petopia.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.UserModel


//펫토피아 뷰모델
class HomePetopiaGuideSharedViewModel () :
    ViewModel() {

        private val user = UserModel()

    //가이드 상태 : "NONE" 모든 가이드 미완료, "ESSENTIAL" 필수 가이드 진행중, "DONE" 가이드 완료, "OPTIONAL" 선택 가이드 진행중
    private val _guideStateLiveData = MutableLiveData("DONE")
    val guideStateLiveData: LiveData<String> = _guideStateLiveData

    //가이드 상태를 업데이트 해주는 함수
    fun updateGuideState(state: String) {
        _guideStateLiveData.value = state
    }







}