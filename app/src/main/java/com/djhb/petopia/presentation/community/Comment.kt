package com.djhb.petopia.presentation.community

import com.djhb.petopia.data.UserModel
import java.time.LocalDateTime

data class Comment(
    val writer: UserModel,
    var content: String,
    val createdDate: LocalDateTime,
    var updatedDate: LocalDateTime
)
