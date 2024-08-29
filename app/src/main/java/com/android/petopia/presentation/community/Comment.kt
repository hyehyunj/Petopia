package com.android.petopia.presentation.community

import com.android.petopia.data.UserModel
import java.time.LocalDateTime

data class Comment(
    val writer: UserModel,
    var content: String,
    val createdDate: LocalDateTime,
    var updatedDate: LocalDateTime
)
