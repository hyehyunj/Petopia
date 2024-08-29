package com.android.petopia.presentation.community

import java.time.LocalDateTime

data class Comment(
    val writer: User,
    var content: String,
    val createdDate: LocalDateTime,
    var updatedDate: LocalDateTime
)
