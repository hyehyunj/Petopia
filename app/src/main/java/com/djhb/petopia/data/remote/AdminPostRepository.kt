package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.AdminPostLocalDataSource
import com.djhb.petopia.data.AdminPostModel
import com.djhb.petopia.data.AlarmLocalDataSource
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.ReportModel
import com.google.firebase.firestore.DocumentSnapshot

interface AdminPostRepository {
    fun getAdminPostLeftData() : List<AdminPostModel>
    fun getAdminPostRightData() : List<AdminPostModel>
}