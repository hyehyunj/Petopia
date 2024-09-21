package com.djhb.petopia.presentation.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetAppearanceModel
import com.djhb.petopia.data.PetLocalDatasource
import com.djhb.petopia.data.PetModel
import com.djhb.petopia.data.remote.GuideRepository
import com.djhb.petopia.data.remote.GuideRepositoryImpl
import com.djhb.petopia.data.remote.PetRepository
import com.djhb.petopia.data.remote.PetRepositoryImpl

//가이드 뷰모델
class MyPetEditViewModel(
    private val guideRepository: GuideRepository,
    private val petRepository: PetRepository
) :
    ViewModel() {

    //반려동물 종 : "DOG" 강아지 , "CAT" 고양이
    private val _breedLiveData = MutableLiveData("DOG")
    val breedLiveData: LiveData<String> = _breedLiveData

    //반려동물 외모
    private val _appearanceListLiveData = MutableLiveData(guideRepository.getPetListData("DOG"))
    val appearanceListLiveData: LiveData<List<PetAppearanceModel>> = _appearanceListLiveData

    private val _appearanceLiveData = MutableLiveData("")
    val appearanceLiveData: LiveData<String> = _appearanceLiveData

    var appearanceMutableListLiveData = _appearanceListLiveData.value?.toMutableList()

    //유저 반려동물 :
    private val _userPetLiveData = MutableLiveData<PetModel?>()
    val userPetLiveData: LiveData<PetModel?> = _userPetLiveData


    //반려동물 외형 불러오는 함수
    fun getAppearanceListData() : List<String> {
        return guideRepository.getAppearanceListData()
    }

    //반려동물 관계 불러오는 함수
    fun getRelationListData() : List<String> {
        return guideRepository.getRelationListData("MY")
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
    fun preparedPetData(): Boolean {
        return petRepository.checkPetData(0) && petRepository.checkPetData(1) && petRepository.checkPetData(2)
    }

}

class MyPetEditViewModelFactory : ViewModelProvider.Factory {
    private val guideRepository = GuideRepositoryImpl(GuideLocalDataSource)
    private val petRepository = PetRepositoryImpl(PetLocalDatasource)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        return MyPetEditViewModel(
            guideRepository,
            petRepository
        ) as T
    }
}