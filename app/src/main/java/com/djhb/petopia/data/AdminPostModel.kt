package com.djhb.petopia.data

import com.djhb.petopia.R
import java.util.UUID

data class AdminPostModel(
    val category: String,
    val page: Int,
    val title: String,
    val background: Int,
    val post: String,
    val uid: String = UUID.randomUUID().toString()
)
