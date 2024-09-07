package com.djhb.petopia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CommentModel(
    val postKey: String = "no postKey",
    val writer: UserModel = UserModel(),
    val content: String = "no content",
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
): Parcelable
