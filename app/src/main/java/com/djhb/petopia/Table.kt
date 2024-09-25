package com.djhb.petopia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Table(val tableName: String): Parcelable {
    USER("user"),
    QUESTION_POST("questionPost"),
    QUESTION_COMMENT("questionComment"),
    QUESTION_LIKE("questionLike"),
    INFORMATION_POST("informationPost"),
    INFORMATION_COMMENT("informationComment"),
    INFORMATION_LIKE("informationLike"),
    GALLERY_POST("galleryPost"),
    GALLERY_COMMENT("galleryComment"),
    GALLERY_LIKE("galleryLike"),
    LETTER("letter"),
    MEMORY("memory"),
    GALLERY("gallery"),
    REPORT("report"),
    NONE("none")
}