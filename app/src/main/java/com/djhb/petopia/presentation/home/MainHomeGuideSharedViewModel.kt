package com.djhb.petopia.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PetLocalDatasource
import com.djhb.petopia.data.PetModel
import com.djhb.petopia.data.remote.PetRepository
import com.djhb.petopia.data.remote.PetRepositoryImpl
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import kotlinx.coroutines.launch


//펫토피아 뷰모델
class MainHomeGuideSharedViewModel(
    private val signRepository: SignRepository,
    private val petRepository: PetRepository
) :
    ViewModel() {

    private val user = LoginData.loginUser

    //가이드 상태 :
    //NONE 가이드 미진행
    //ESSENTIAL 필수 가이드 진행중
    //ESSENTIAL_DONE 필수 가이드 완료
    //OPTIONAL 선택 가이드 진행중
    //DONE 모두 완료
    private val _guideStateLiveData = MutableLiveData("NONE")
    val guideStateLiveData: LiveData<String> = _guideStateLiveData

    //가이드 기능설명 :
    // "PETOPIA" "GALLERY_LETTER" "D_DAY" "MOVE_MEMORY_BRIDGE"
    // "MEMORY" "EMOTION" "MOVE_EARTH"
    // "CLOUD" "COMMUNITY" "MOVE_UPPER" "MY" "END"
    private val _guideFunctionLiveData = MutableLiveData("")
    val guideFunctionLiveData: LiveData<String> = _guideFunctionLiveData

    //현재 홈 :
    private val _currentHomeLiveData = MutableLiveData(0)
    val currentHomeLiveData: LiveData<Int> = _currentHomeLiveData

    //유저 반려동물 :
    private val _userPetLiveData = MutableLiveData<PetModel?>()
    val userPetLiveData: LiveData<PetModel?> = _userPetLiveData


    //가이드 상태를 업데이트 해주는 함수
    fun updateGuideState(state: String) {
        _guideStateLiveData.value = state
    }

    //가이드 기능설명을 업데이트 해주는 함수
    fun updateGuideFunction(function: String) {
        _guideFunctionLiveData.value = function
    }


    //유저 정보를 불러오는 함수
    fun getUser(): Boolean {
        var skipGuide: Boolean
        if (user.completedGuide) {
            getPetData()
            _guideStateLiveData.value = "DONE"
            skipGuide = true
        } else {
            _guideStateLiveData.value = "NONE"
            skipGuide = false
        }
        _userPetLiveData.value = user.pet
        return skipGuide
    }

    //유저 정보를 입력하는 함수
    fun setPetData() {
        _userPetLiveData.value = petRepository.getPetData().copy(id = user.id)
        user.pet = _userPetLiveData.value
        user.completedGuide = true
        viewModelScope.launch {
            signRepository.updateUser(user)
        }
    }

    //유저 정보를 불러오는 함수
    private fun getPetData() {
        _userPetLiveData.value = user.pet
    }



    //유저 이름을 불러오는 함수
    fun getUserName(): String {
        return user.nickname
    }


    fun updateCurrentHome(fragmentPosition: Int) {
        _currentHomeLiveData.value = fragmentPosition
    }


    //가이드에서 설명하는 기능을 업데이트 해주는 함수
//    fun updateFunction(function: Int) {
//        val guideFunctionData = mapOf(
//            9 to "GALLERY_LETTER_GONE",
//            10 to "GALLERY_LETTER",
//            11 to "D_DAY",
//            13 to "MOVE_MEMORY_BRIDGE",
//            14 to "MEMORY",
//            15 to "EMOTION",
//            17 to "MOVE_EARTH",
//            18 to "CLOUD",
//            19 to "COMMUNITY",
//            20 to "MOVE_UPPER",
//            21 to "MY",
//            22 to "END"
//        )
//        _guideFunctionLiveData.value = guideFunctionData[function]
//    }


}

class MainHomeGuideSharedViewModelFactory : ViewModelProvider.Factory {
    private val signRepository = SignRepositoryImpl()
    private val petRepository = PetRepositoryImpl(PetLocalDatasource)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        return MainHomeGuideSharedViewModel(
            signRepository,
            petRepository
        ) as T
    }
}