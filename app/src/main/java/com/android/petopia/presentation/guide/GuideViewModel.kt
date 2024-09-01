package com.android.petopia.presentation.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.GuideModel
import com.android.petopia.data.PetModel

//가이드 뷰모델
class GuideViewModel :
    ViewModel() {

    private val dog = listOf("말티즈", "푸들 비숑", "치와와", "포메라니안", "웰시코기", "시츄", "시바", "진돗개", "리트리버")
    private val cat = listOf("고양이", "푸들 비숑", "치와와", "포메라니안", "웰시코기", "시츄", "시바", "진돗개", "리트리버")

    //페이지
    private val _guidePageNumberLiveData = MutableLiveData(0)
    val guidePageNumberLiveData: LiveData<Int> = _guidePageNumberLiveData

    //가이드데이터클래스 : 상단진행글, 상단진행바, 스토리, 다이얼로그
    private val _guideModelLiveData = MutableLiveData<GuideModel>()
    val guideModelLiveData: LiveData<GuideModel> = _guideModelLiveData

    //버튼 : "BACK" 뒤로가기, "NEXT" 다음으로
    private val _pressedButtonLiveData = MutableLiveData("")
    val pressedButtonLiveData: LiveData<String> = _pressedButtonLiveData

    //반려동물 데이터클래스
    private val _petModelLiveData = MutableLiveData<PetModel>()
    val petModelLiveData: LiveData<PetModel> = _petModelLiveData

    //종 대분류 : "DOG" 강아지 , "CAT" 고양이
    private val _appearanceLiveData = MutableLiveData("DOG")
    val appearanceLiveData: LiveData<String> = _appearanceLiveData

    //종 소분류
    private val _breedListLiveData = MutableLiveData(dog)
    val breedListLiveData: LiveData<List<String>> = _breedListLiveData

    fun changeAppearance(appearance: String) {
        _appearanceLiveData.value = appearance
    }

    fun changeBreed() {
        _breedListLiveData.value = if(_appearanceLiveData.value == "DOG") dog else cat
    }


//반려동물 정보
    //반려동물 이름을 입력받는 함수
    fun setPetName(petName: String) {
        _petModelLiveData.value = PetModel(petName, "", 0,"",0,0)
        guideButtonClickListener("NEXT")
    }

    //반려동물 외모를 입력받는 함수
    fun setPetAppearance(breed : String) {
        _petModelLiveData.value = _petModelLiveData.value?.copy(petAppearance = breed)
    }












    //클릭된 버튼에 따라 페이지 변경해주는 함수
    fun guideButtonClickListener(pressedButton: String) {
        _pressedButtonLiveData.value = pressedButton
        when (_pressedButtonLiveData.value) {
            "BACK" -> if (_guidePageNumberLiveData.value != -1) _guidePageNumberLiveData.value =
                _guidePageNumberLiveData.value?.minus(1)
            "NEXT" -> if (_guidePageNumberLiveData.value == -1) _guidePageNumberLiveData.value = _guidePageNumberLiveData.value?.plus(2)
                else _guidePageNumberLiveData.value = _guidePageNumberLiveData.value?.plus(1)
        }
    }

    //
    private val progressBarData = listOf(0, 1, 2)

    //
    private val progressTextData = listOf("보호자", "이름", "비슷한", "화면")

    //
    private val guideStoryData = mapOf(
        0 to "누구를 찾아오셨나요?",
        2 to "어떤 외모",
        4 to "처럼",
        6 to "도와",
        8 to "보시는",
        9 to "펫토피아",
        10 to "편지나 사진은",
        11 to "소중한 날짜를",
        12 to "메모리 브릿지로",
        )

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
        if(page == 8) {
            _guideModelLiveData.value = guideModel.copy(completeFirstGuide = true)
        }

    }


}
