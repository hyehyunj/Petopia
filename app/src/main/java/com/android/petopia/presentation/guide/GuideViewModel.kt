package com.android.petopia.presentation.guide

import android.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.GuideModel

//가이드 뷰모델
class GuideViewModel :
    ViewModel() {


    //페이지
    private val _guidePageNumberLiveData = MutableLiveData(0)
    val guidePageNumberLiveData: LiveData<Int> = _guidePageNumberLiveData

    //가이드데이터클래스 : 상단진행글, 상단진행바, 스토리, 다이얼로그
    private val _guideModelLiveData = MutableLiveData<GuideModel>()
    val guideModelLiveData: LiveData<GuideModel> = _guideModelLiveData

    //버튼 : "BACK" 뒤로가기, "NEXT" 다음으로
    private val _pressedButtonLiveData = MutableLiveData("")
    val pressedButtonLiveData: LiveData<String> = _pressedButtonLiveData


    //클릭된 버튼에 따라 페이지 변경해주는 함수
    fun guideButtonClickListener(pressedButton: String) {
        _pressedButtonLiveData.value = pressedButton
        when (_pressedButtonLiveData.value) {
            "BACK" -> if (_guidePageNumberLiveData.value != 0) _guidePageNumberLiveData.value =
                _guidePageNumberLiveData.value?.minus(1)
            "NEXT" -> _guidePageNumberLiveData.value = _guidePageNumberLiveData.value?.plus(1)
        }
    }

    //
    private val progressBarData = listOf(0, 1, 2)

    //
    private val progressTextData = listOf("보호자", "이름", "비슷한", "화면")

    //
    private val guideStoryData = mapOf(
        0 to "누구를 찾아오셨나요?", 2 to "어떤 외모", 4 to "처럼", 6 to "도와")

    //
    private val guideDialogData = mapOf(
        1 to 0, 3 to 1, 5 to 2)


    //가이드화면 만드는 함수
    fun makeGuideModel() {
        val page = _guidePageNumberLiveData.value ?: 0
        var progressBar = 0
        var progressText = ""
        var guideStory = ""
        var guideDialog = 9

        if(guideStoryData[page] != null) guideStory = guideStoryData[page].toString()
        if(guideDialogData[page] != null) guideDialog = guideDialogData[page]!!

        //페이지에 따라 상단 진행바, 진행문구 변경
        when (page) {
            in 0..1 -> progressText = progressTextData[0]
            in 2..3 -> {
                progressBar = progressBarData[0]
                progressText = progressTextData[1]
            }

            in 4..5 -> {
                progressBar = progressBarData[1]
                progressText = progressTextData[2]
            }

            in 6..7 -> {
                progressBar = progressBarData[2]
                progressText = progressTextData[3]
            }
        }

        val guideModel = GuideModel(
            progressBar,
            progressText,
            guideStory,
            guideDialog
        )
        _guideModelLiveData.value = guideModel

    }


}
