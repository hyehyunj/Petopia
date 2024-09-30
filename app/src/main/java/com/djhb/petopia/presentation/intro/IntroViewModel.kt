package com.djhb.petopia.presentation.intro

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.AdminPostModel
import com.djhb.petopia.data.DDayModel
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
import kotlin.math.truncate

//인트로 뷰모델
class IntroViewModel(private val introRepository: IntroRepository) :
    ViewModel() {

    //인트로 리스트를 불러오는 함수
    fun loadInitIntroList() : List<IntroModel> {
        return introRepository.getIntroData()
    }

    //인트로 스킵여부를 불러오는 함수
    fun loadIntroSkipData(context: Context) : Boolean {
        return introRepository.loadIntroSkip(context)
    }

    //인트로 스킵여부를 저장하는 함수
    fun updateIntroSkipData(context: Context) {
        viewModelScope.launch {
            introRepository.updateIntroSkip(context, false)
        }
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