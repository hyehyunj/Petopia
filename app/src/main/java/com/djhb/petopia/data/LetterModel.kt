package com.djhb.petopia.data

import com.djhb.petopia.LetterType
import java.util.UUID

data class LetterModel(
    val title: String,
    val content: String,
    val writer: UserModel,
    val letterType: LetterType = LetterType.NORMAL,
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)