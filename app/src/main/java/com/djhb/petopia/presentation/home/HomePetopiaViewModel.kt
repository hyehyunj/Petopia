package com.djhb.petopia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.HomePetopiaLocalDataSource
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.HomePetopiaRepository
import com.djhb.petopia.data.remote.HomePetopiaRepositoryImpl


//펫토피아 뷰모델
class HomePetopiaViewModel (private val homePetopiaRepository: HomePetopiaRepository) :
    ViewModel() {

fun getPetMassage() : String {
    return LoginData.loginUser.pet?.petRelation?.let { homePetopiaRepository.getPetMassage(it) }.toString()
}


}
class HomePetopiaViewModelFactory : ViewModelProvider.Factory {
    private val homePetopiaRepository = HomePetopiaRepositoryImpl(HomePetopiaLocalDataSource)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        return HomePetopiaViewModel(
            homePetopiaRepository
        ) as T
    }
}