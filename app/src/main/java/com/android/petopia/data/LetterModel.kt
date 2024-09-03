package com.android.petopia.data

data class LetterModel(
    var title: String,
    var content: String,
    val writer: UserModel
)
