package com.djhb.petopia.presentation.admin

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.data.remote.AdminRepository
import com.djhb.petopia.data.remote.AdminRepositoryImpl
import com.djhb.petopia.data.remote.GalleryRepository
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//갤러리와 포토의 공유 뷰모델
class AdminViewModel(private val adminRepository: AdminRepository) :
    ViewModel() {
    private val user = LoginData.loginUser

    //신고 리스트
    private val _reportListLiveData = MutableLiveData<List<ReportModel>>(listOf())
    val reportListLiveData: LiveData<List<ReportModel>> = _reportListLiveData
    private var reportMutableList: MutableList<ReportModel> =
        _reportListLiveData.value?.toMutableList() ?: mutableListOf()

    //신고 상세페이지
    private val _reportDetailLiveData = MutableLiveData<ReportModel>()
    val reportDetailLiveData: LiveData<ReportModel> = _reportDetailLiveData


}


class AdminViewModelFactory : ViewModelProvider.Factory {
    private val repository =
        AdminRepositoryImpl()

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return AdminViewModel(
            repository
        ) as T
    }
}