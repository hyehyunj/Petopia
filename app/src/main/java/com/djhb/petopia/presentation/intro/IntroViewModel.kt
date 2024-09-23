package com.djhb.petopia.presentation.intro

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.AdminPostModel
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.IntroLocalDataSource
import com.djhb.petopia.data.IntroModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.data.remote.AdminRepository
import com.djhb.petopia.data.remote.AdminRepositoryImpl
import com.djhb.petopia.data.remote.CommentRepository
import com.djhb.petopia.data.remote.CommentRepositoryImpl
import com.djhb.petopia.data.remote.DDayRepository
import com.djhb.petopia.data.remote.GalleryRepository
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
import com.djhb.petopia.data.remote.IntroRepository
import com.djhb.petopia.data.remote.IntroRepositoryImpl
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.sign

//관리자게시글 공유 뷰모델
class IntroViewModel(private val introRepository: IntroRepository) :
    ViewModel() {
    private val user = LoginData.loginUser

    // 배웅하기 리스트
    private val _adminPostLeftListLiveData = MutableLiveData<List<AdminPostModel>>(listOf())
    val adminPostLeftListLiveData: LiveData<List<AdminPostModel>> = _adminPostLeftListLiveData

    // 잘지내기 리스트
    private val _adminPostRightListLiveData = MutableLiveData<List<AdminPostModel>>(listOf())
    val adminPostRightListLiveData: LiveData<List<AdminPostModel>> = _adminPostRightListLiveData


    //소개글 불러오기
    fun loadInitIntroList() : List<IntroModel> {
        return introRepository.getIntroData()
    }


}


class IntroViewModelFactory : ViewModelProvider.Factory {
    private val introRepository = IntroRepositoryImpl(IntroLocalDataSource)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return IntroViewModel(
            introRepository
        ) as T
    }
}