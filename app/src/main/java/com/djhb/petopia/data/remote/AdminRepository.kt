package com.djhb.petopia.data.remote

import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.ReportModel
import com.google.firebase.firestore.DocumentSnapshot

interface AdminRepository {

    suspend fun createReport(report: ReportModel)
    suspend fun selectInitAllReports(): List<DocumentSnapshot>
    suspend fun selectAllReports(lastSnapshot: DocumentSnapshot): List<DocumentSnapshot>
//    suspend fun selectReport(reportKey: String): ReportModel
    suspend fun deleteReport(reportKey: String)
    fun convertToReportModel(documents: List<DocumentSnapshot>): MutableList<ReportModel>

}