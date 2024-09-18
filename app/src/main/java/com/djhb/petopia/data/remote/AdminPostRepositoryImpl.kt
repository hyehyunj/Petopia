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

class AdminPostRepositoryImpl: AdminPostRepository {

}