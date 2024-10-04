package com.djhb.petopia.presentation.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetAppearanceModel
import com.djhb.petopia.data.PetLocalDatasource
import com.djhb.petopia.data.remote.GuideRepository
import com.djhb.petopia.data.remote.GuideRepositoryImpl
import com.djhb.petopia.data.remote.PetRepository
import com.djhb.petopia.data.remote.PetRepositoryImpl

//가이드 뷰모델
class GuideSharedViewModel(
    private val guideRepository: GuideRepository,
    private val petRepository: PetRepository
) :
    ViewModel() {

    //페이지
    private val _guidePageNumberLiveData = MutableLiveData<Int>(0)
    val guidePageNumberLiveData: LiveData<Int> = _guidePageNumberLiveData

    //가이드데이터클래스
    private val _guideModelLiveData = MutableLiveData<GuideModel>()
    val guideModelLiveData: LiveData<GuideModel> = _guideModelLiveData

    //버튼 : "BACK" 뒤로가기, "NEXT" 다음으로
    private val _pressedButtonLiveData = MutableLiveData("")
    val pressedButtonLiveData: LiveData<String> = _pressedButtonLiveData

    //반려동물 종 : "DOG" 강아지 , "CAT" 고양이
    private val _breedLiveData = MutableLiveData("DOG")
    val breedLiveData: LiveData<String> = _breedLiveData

    //반려동물 외모 리스트
    private val _appearanceListLiveData = MutableLiveData(guideRepository.getPetListData("DOG"))
    val appearanceListLiveData: LiveData<List<PetAppearanceModel>> = _appearanceListLiveData

    //반려동물 외모(선택값)
    private val _appearanceLiveData = MutableLiveData("")
    val appearanceLiveData: LiveData<String> = _appearanceLiveData
    private var appearanceMutableListLiveData = _appearanceListLiveData.value?.toMutableList()

//가이드 재실행시 시작 페이지를 정해주는 함수
fun setWelcomeGuidePage() {
     _guidePageNumberLiveData.value = 8
}

    //클릭된 버튼에 따라 페이지 변경해주는 함수
    fun guideButtonClickListener(pressedButton: String) {
        _pressedButtonLiveData.value = pressedButton
        when (_pressedButtonLiveData.value) {
            "BACK" -> if (_guidePageNumberLiveData.value != -1) _guidePageNumberLiveData.value =
                _guidePageNumberLiveData.value?.minus(1)

            "NEXT" -> if (_guidePageNumberLiveData.value == -1) _guidePageNumberLiveData.value =
                _guidePageNumberLiveData.value?.plus(2)
            else _guidePageNumberLiveData.value = _guidePageNumberLiveData.value?.plus(1)
        }
    }

    //가이드화면 만드는 함수
    fun makeGuideModel() {
        val page = _guidePageNumberLiveData.value ?: 0
        _guideModelLiveData.value = page?.let { guideRepository.getGuideData(it) }
    }

    //반려동물 종 분류 입력받는 함수
    fun changeBreed(breed: String) {
        _breedLiveData.value = breed
    }

    //반려동물 외형 분류 입력받는 함수
    fun changeAppearance() {
        _appearanceListLiveData.value =
            _breedLiveData.value?.let { guideRepository.getPetListData(it) }
    }

    //반려동물 이름을 입력받는 함수
    fun setPetName(petName: String) {
        petRepository.setPetNameData(petName)
        guideButtonClickListener("NEXT")
    }

    //클릭된 리스트 변경해주는 함수
    fun updateSelected(petAppearance: PetAppearanceModel, position: Int) {
        appearanceMutableListLiveData = mutableListOf()
        _appearanceListLiveData.value?.toMutableList()?.forEach { appearanceMutableListLiveData?.plusAssign(
            it.copy(selected = false)
        ) }
        appearanceMutableListLiveData?.set(position, petAppearance)
        _appearanceListLiveData.value = appearanceMutableListLiveData
    }

    //반려동물 외모를 입력받는 함수
    fun setPetAppearance(petAppearance: String) {
        petRepository.setPetAppearanceData(petAppearance)[1]
        _appearanceLiveData.value = petAppearance
    }

    //반려동물 이미지를 입력받는 함수
    fun setPetRelation(petRelation: String) {
        petRepository.setPetRelationData(petRelation)[2]
    }

    //반려동물 데이터가 준비됐는지 확인하는 함수
    fun preparedPetData(index: Int): Boolean {
        return petRepository.checkPetData(index)
    }

}

class GuideSharedViewModelFactory : ViewModelProvider.Factory {
    private val guideRepository = GuideRepositoryImpl(GuideLocalDataSource)
    private val petRepository = PetRepositoryImpl(PetLocalDatasource)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        return GuideSharedViewModel(
            guideRepository,
            petRepository
        ) as T
    }
}