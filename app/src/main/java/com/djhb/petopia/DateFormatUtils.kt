package com.djhb.petopia

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatUtils {

    val letterFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    val postFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    val imageFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)

    fun convertToLetterFormat(milliSeconds: Long): String {
        return letterFormat.format(milliSeconds)
    }

    fun convertToPostFormat(milliSeconds: Long): String {
        return postFormat.format(milliSeconds)
    }

    fun convertToImageFormat(milliSeconds: Long): String {
        return imageFormat.format(milliSeconds)
    }
}