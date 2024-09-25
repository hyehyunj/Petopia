package com.djhb.petopia.presentation.admin.post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.AdminPostLocalDataSource
import com.djhb.petopia.data.AdminPostModel
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.data.remote.AdminPostRepository
import com.djhb.petopia.data.remote.AdminPostRepositoryImpl
import com.djhb.petopia.data.remote.AdminRepository
import com.djhb.petopia.data.remote.AdminRepositoryImpl
import com.djhb.petopia.data.remote.CommentRepository
import com.djhb.petopia.data.remote.CommentRepositoryImpl
import com.djhb.petopia.data.remote.GalleryRepository
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
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
class AdminPostViewModel(private val adminPostRepository : AdminPostRepository) :
    ViewModel() {
    private val user = LoginData.loginUser

    // 배웅하기 리스트
    private val _adminPostLeftListLiveData = MutableLiveData<List<AdminPostModel>>(listOf())
    val adminPostLeftListLiveData: LiveData<List<AdminPostModel>> = _adminPostLeftListLiveData

    // 잘지내기 리스트
    private val _adminPostRightListLiveData = MutableLiveData<List<AdminPostModel>>(listOf())
    val adminPostRightListLiveData: LiveData<List<AdminPostModel>> = _adminPostRightListLiveData


    //관리자 게시글 불러오기
    fun getAdminPostLeftList() : List<AdminPostModel> {
   return adminPostRepository.getAdminPostLeftData()
    }

    fun getAdminPostRightList() : List<AdminPostModel> {
        return adminPostRepository.getAdminPostRightData()
    }
}


class AdminPostViewModelFactory : ViewModelProvider.Factory {
    private val adminPostRepository = AdminPostRepositoryImpl(AdminPostLocalDataSource)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return AdminPostViewModel(
            adminPostRepository
        ) as T
    }
}