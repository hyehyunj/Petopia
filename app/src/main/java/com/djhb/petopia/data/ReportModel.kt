package com.djhb.petopia.data

import com.djhb.petopia.BlockType
import com.djhb.petopia.ReportContentType
import com.djhb.petopia.ReportReasonType

data class ReportModel(
    val uid: String,
    val reporterId: String,
    val targetUserId: String,
    val blockType: BlockType,
    val reasonType: ReportReasonType,
    val reasonDescription: String,
    val contentType: ReportContentType,
    val contentUid: String,
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long? = null
)
