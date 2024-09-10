package com.djhb.petopia.data

import android.os.Parcelable
import com.djhb.petopia.BlockType
import com.djhb.petopia.ReportContentType
import com.djhb.petopia.ReportReasonType
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ReportModel(
    val reporterId: String = "",
    val targetUserId: String = "",
//    val blockType: BlockType,
    val reasonType: ReportReasonType = ReportReasonType.ETC,
    val reasonDescription: String = "",
    val contentType: ReportContentType = ReportContentType.QUESTION_POST,
    val contentUid: String = "",
    val uid: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long? = null
) : Parcelable
