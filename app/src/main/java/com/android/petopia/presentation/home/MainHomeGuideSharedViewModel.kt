package com.android.petopia.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.UserModel


//펫토피아 뷰모델
class MainHomeGuideSharedViewModel () :
    ViewModel() {

        private val user = UserModel()

    //가이드 상태 : "NONE" 모든 가이드 미완료, "ESSENTIAL" 필수 가이드 진행중, "DONE" 가이드 완료, "OPTIONAL" 선택 가이드 진행중
    private val _guideStateLiveData = MutableLiveData("NONE")
    val guideStateLiveData: LiveData<String> = _guideStateLiveData

    private val _guideFunctionLiveData = MutableLiveData("NONE")
    val guideFunctionLiveData: LiveData<String> = _guideFunctionLiveData
    //가이드 기능설명 : "NONE"
    // "GALLERY_LETTER" "D_DAY"
    // "MEMORY" "EMOTION" "MOVE_UNDER"
    // "CLOUD" "COMMUNITY" "MOVE_UPPER" "MY" "END"



    //가이드 상태를 업데이트 해주는 함수
    fun updateGuideState(state: String) {
        _guideStateLiveData.value = state
    }




    //가이드에서 설명하는 기능을 업데이트 해주는 함수
    fun updateFunction(function: Int) {
        val guideFunctionData = mapOf(
            10 to "GALLERY_LETTER",
            11 to "D_DAY",
            13 to "MOVE_UNDER",
            14 to "MEMORY",
            15 to "EMOTION",
            18 to "MOVE_UNDER",
            19 to "CLOUD",
            19 to "COMMUNITY",
            20 to "MOVE_UPPER",
            21 to "MY",
            23 to "END"
        )
        _guideFunctionLiveData.value = guideFunctionData[function]
    }






}