package com.djhb.petopia.data.remote

import android.util.Log
import com.djhb.petopia.Table
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.ReportModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AdminRepositoryImpl: AdminRepository {

    private val reference = Firebase.firestore.collection(Table.REPORT.tableName)

    override suspend fun createReport(report: ReportModel) {
       withContext(Dispatchers.IO) {
            reference.document(report.uid).set(report).addOnCompleteListener {
                Log.i("AdminRepositoryImpl", "success create report")
            }.addOnFailureListener {
                Log.i("AdminRepositoryImpl", "fail create report = ${it}")
            }
        }
    }

    override suspend fun selectInitAllReports(): List<DocumentSnapshot> {

        return withContext(Dispatchers.IO) {
            val await = reference
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            await.documents
        }
    }

    override suspend fun selectAllReports(lastSnapshot: DocumentSnapshot): List<DocumentSnapshot> {

        return withContext(Dispatchers.IO) {
            val await = reference
                .startAfter(lastSnapshot)
                .orderBy("createdDate", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            await.documents
        }
    }

//    override suspend fun selectReport(reportKey: String): ReportModel {
//        reference.document(reportKey).get().
//    }

    override suspend fun deleteReport(reportKey: String) {
        withContext(Dispatchers.IO) {
            reference.document(reportKey).delete()
        }
    }


    override fun convertToReportModel(documents: List<DocumentSnapshot>): MutableList<ReportModel> {

            val reportList = mutableListOf<ReportModel>()

            for (document in documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, ReportModel::class.java)
                reportList.add(fromJson)

            }
        return reportList
    }
}