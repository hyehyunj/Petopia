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

//갤러리와 포토의 공유 뷰모델
class AdminViewModel :
    ViewModel() {
    private val user = LoginData.loginUser

    //신고 리스트
    private val _reportListLiveData = MutableLiveData<List<ReportModel>>(listOf())
    val reportListLiveData: LiveData<List<ReportModel>> = _reportListLiveData
    private var reportMutableList: MutableList<ReportModel> =
        _reportListLiveData.value?.toMutableList() ?: mutableListOf()

    private val reportListResult = mutableListOf<ReportModel>()

    //신고 상세페이지
    private val _reportDetailLiveData = MutableLiveData<ReportModel>()
    val reportDetailLiveData: LiveData<ReportModel> = _reportDetailLiveData

    private lateinit var lastSnapshot: DocumentSnapshot

    private val adminRepository: AdminRepository by lazy {
        AdminRepositoryImpl()
    }

    private val signRepository: SignRepository by lazy {
        SignRepositoryImpl()
    }

    private val postRepository: PostRepository by lazy {
        PostRepositoryImpl()
    }

    private val commentRepository: CommentRepository by lazy {
        CommentRepositoryImpl()
    }


    fun selectReport(report: ReportModel) {
        _reportDetailLiveData.value = report
    }

    //신고리스트 불러오기
    fun loadInitReportList() {
        viewModelScope.launch {
            val documents = adminRepository.selectInitAllReports()
            if(documents.size > 0 ) {
                reportListResult.clear()
                lastSnapshot = documents[documents.size - 1]
                reportListResult.addAll(adminRepository.convertToReportModel(documents))
                _reportListLiveData.value = reportListResult
            }
            //신고리스트 결과 담기
//            _reportListLiveData.value =
        }
    }

    suspend fun loadReportList() {
        viewModelScope.launch {
            val documents = adminRepository.selectAllReports(lastSnapshot)
            if(documents.size > 0) {
                reportListResult.clear()
                lastSnapshot = documents[documents.size - 1]
                reportListResult.addAll(adminRepository.convertToReportModel(documents))
                _reportListLiveData.value = reportListResult
            }
            //신고리스트 결과 담기
//            _reportListLiveData.value =
        }
    }

    fun deleteReport(report: ReportModel){
        viewModelScope.launch {
            adminRepository.deleteReport(report.uid)
            signRepository.deleteUser(report.targetUserId)
            val posts = postRepository.selectPostFromUser(report.targetUserId)
            for (post in posts) {
                postRepository.deletePost(post.key)
                postRepository.deletePostImages(post.key)
            }

            val comments =
                commentRepository.selectAllCommentsFromUser(report.targetUserId)

            for (comment in comments) {
                commentRepository.deleteComment(comment.key)
            }

            reportListResult.removeIf{
                it.uid == report.uid
            }

            _reportListLiveData.value = reportListResult
        }
    }





}


//class AdminViewModelFactory : ViewModelProvider.Factory {
//    private val repository =
//        AdminRepositoryImpl()
//
//    override fun <T : ViewModel> create(
//        modelClass: Class<T>,
//        extras: CreationExtras
//    ): T {
//        return AdminViewModel(
//            repository
//        ) as T
//    }
//}