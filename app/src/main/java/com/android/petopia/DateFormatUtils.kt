package com.android.petopia

import java.text.SimpleDateFormat

object DateFormatUtils {

    val letterFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    val postFormat = SimpleDateFormat("yyyy.MM.dd")

    fun convertToLetterFormat(milliSeconds: Long): String {
        return letterFormat.format(milliSeconds)
    }

    fun convertToPostFormat(milliSeconds: Long): String {
        return postFormat.format(milliSeconds)
    }

}