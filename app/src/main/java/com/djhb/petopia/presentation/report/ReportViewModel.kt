package com.djhb.petopia.presentation.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djhb.petopia.ReportContentType
import com.djhb.petopia.data.ReportModel

class ReportViewModel : ViewModel() {
    private val _reportReason = MutableLiveData<String>()
    val reportReason get() = _reportReason

    private val _reportReasonDetail = MutableLiveData<String>()
    val reportReasonDetail get() = _reportReasonDetail

    private val _reportModelLiveData = MutableLiveData<ReportModel>()
    val reportModelLiveData get() = _reportModelLiveData


    //신고한유저는 usermodel

    // 신고대상자는?

    fun setReportReason(reason: String) {
        _reportReason.value = reason
    }

    fun setContent(type: ReportContentType, uid: String) {
        val updateContent = _reportModelLiveData.value!!.copy(contentType = type, contentUid = uid)
        _reportModelLiveData.value = updateContent

    }

    fun setReportReasonDetail(detail: String) {
        _reportReasonDetail.value = detail
    }



}