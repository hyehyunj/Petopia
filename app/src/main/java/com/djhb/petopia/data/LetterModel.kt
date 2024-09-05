package com.djhb.petopia.data

import com.djhb.petopia.LetterType
import java.util.UUID

data class LetterModel(
    var title: String,
    var content: String,
    val writer: UserModel,
    val letterType: LetterType = LetterType.NORMAL,
    var key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null
)