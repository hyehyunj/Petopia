package com.djhb.petopia.presentation.my

import android.view.ViewDebug.IntToString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.R
import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PetAppearanceModel
import com.djhb.petopia.data.PetLocalDatasource
import com.djhb.petopia.data.PetModel
import com.djhb.petopia.data.remote.GuideRepository
import com.djhb.petopia.data.remote.GuideRepositoryImpl
import com.djhb.petopia.data.remote.PetRepository
import com.djhb.petopia.data.remote.PetRepositoryImpl
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import kotlinx.coroutines.launch

//가이드 뷰모델
class MyPetEditViewModel(
    private val guideRepository: GuideRepository,
    private val petRepository: PetRepository,
    private val signRepository: SignRepository
) :
    ViewModel() {

    //반려동물 종 : "DOG" 강아지 , "CAT" 고양이
    private val _breedLiveData = MutableLiveData("DOG")
    val breedLiveData: LiveData<String> = _breedLiveData

    //반려동물 외모
    private val _appearanceLiveData = MutableLiveData("")
    val appearanceLiveData: LiveData<String> = _appearanceLiveData

    //유저 반려동물 :
    private val _userPetLiveData = MutableLiveData<PetModel?>()
    val userPetLiveData: LiveData<PetModel?> = _userPetLiveData

    //반려동물 정보 불러오는 함수
    fun getUserPetData() : PetModel? {
        return LoginData.loginUser.pet
    }

    //반려동물 외형 리스트 불러오는 함수
    fun getAppearanceListData() : List<String> {
        return guideRepository.getAppearanceListData()
    }

    //반려동물 관계 리스트 불러오는 함수
    fun getRelationListData() : List<String> {
        return guideRepository.getRelationListData("MY")
    }

    //반려동물 외모를 입력받는 함수
    fun setPetAppearance(position: Int) {
        var intToString = ""
        when (position) {
            0 -> intToString = "리트리버"
            1 -> intToString = "말티즈"
            2 -> intToString = "비숑"
            3 -> intToString = "시바"
            4 -> intToString = "시츄"
            5 -> intToString = "웰시코기"
            6 -> intToString = "치와와"
            7 -> intToString = "포메라니안"
            8 -> intToString = "푸들"
            9 -> intToString = "노르웨이숲"
            10 -> intToString = "러시안블루"
            11 -> intToString = "샴"
            12 -> intToString = "스코티쉬폴드"
            13 -> intToString = "아메리칸숏헤어"
            14 -> intToString = "아비시니안"
            15 -> intToString = "코리안숏헤어"
            16 -> intToString = "터키쉬앙고라"
            17 -> intToString = "페르시안"
        }
        petRepository.setPetAppearanceData(intToString)[1]
        _appearanceLiveData.value = intToString
    }

    //반려동물 관계를 입력받는 함수
    fun setPetRelation(position: Int) {
        var intToString = ""
        when (position) {
            0 -> intToString = "CHILD"
            1 -> intToString = "YOUNGER"
            2 -> intToString = "FRIEND"
        }
        petRepository.setPetRelationData(intToString)[2]
    }

    //반려동물 이름을 입력받는 함수
    fun setPetName(petName: String) {
        petRepository.setPetNameData(petName)
    }

    //반려동물 데이터가 준비됐는지 확인하는 함수
    fun preparedPetData(): Boolean {
        return petRepository.checkPetData(0) && petRepository.checkPetData(1) && petRepository.checkPetData(2)
    }

    //유저 정보를 입력하는 함수
    fun setPetData() {
        _userPetLiveData.value = petRepository.getPetData().copy(id = LoginData.loginUser.id)
        LoginData.loginUser.pet = _userPetLiveData.value
        viewModelScope.launch {
            signRepository.updateUser(LoginData.loginUser)
        }
    }






}

class MyPetEditViewModelFactory : ViewModelProvider.Factory {
    private val guideRepository = GuideRepositoryImpl(GuideLocalDataSource)
    private val petRepository = PetRepositoryImpl(PetLocalDatasource)
    private val signRepository = SignRepositoryImpl()
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        return MyPetEditViewModel(
            guideRepository,
            petRepository,
            signRepository
        ) as T
    }
}