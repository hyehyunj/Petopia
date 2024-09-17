package com.djhb.petopia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class PostModel(
    val title: String = "",
    val content: String = "",
    val writer: UserModel = UserModel(),
    val imageUris: MutableList<String> = mutableListOf(),
    val likes: MutableList<LikeModel> = mutableListOf(),
    val key: String = UUID.randomUUID().toString(),
    val viewCount: Int = 0,
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long? = null,
) : Parcelable
