package com.djhb.petopia.presentation.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.ReportContentType
import com.djhb.petopia.ReportReasonType
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.data.remote.AdminRepository
import com.djhb.petopia.data.remote.AdminRepositoryImpl
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {
    private val _reportReason = MutableLiveData<ReportReasonType>()
    val reportReason get() = _reportReason

    private val _reportReasonDetail = MutableLiveData<String>()
    val reportReasonDetail get() = _reportReasonDetail

    private val _reportModelLiveData = MutableLiveData<ReportModel>()
    val reportModelLiveData get() = _reportModelLiveData

    private var reportModelResult = ReportModel()

    private val adminRepository: AdminRepository by lazy{
        AdminRepositoryImpl()
    }


    fun createReport(){
        if(_reportModelLiveData.value != null) {
            viewModelScope.launch {
                adminRepository.createReport(_reportModelLiveData.value!!)
            }
        }
    }


    //신고한유저는 usermodel

    // 신고대상자는?

    fun setReportReason(reason: ReportReasonType) {
        _reportReason.value = reason
//        _reportReason.value = reportModelResult.copy(reasonType = reason)
    }

    fun setContent(report: ReportModel) {
//        val updateContent = _reportModelLiveData.value!!.copy(contentType = type, contentUid = uid)
        _reportModelLiveData.value = report
    }

    fun setReportReasonDetail(detail: String) {
        _reportReasonDetail.value = detail
    }



}