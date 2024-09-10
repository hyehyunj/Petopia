package com.djhb.petopia.presentation.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {
    private val _reportReason = MutableLiveData<String>()
    val reportReason get() = _reportReason

    private val _reportReasonDetail = MutableLiveData<String>()
    val reportReasonDetail get() = _reportReasonDetail

    //신고한유저는 usermodel

    // 신고대상자는?

    fun setReportReason(reason: String) {
        _reportReason.value = reason
    }

    fun setReportReasonDetail(detail: String) {
        _reportReasonDetail.value = detail
    }



}