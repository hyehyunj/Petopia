package com.djhb.petopia.data

data class GuideModel(
    val progressBar: Int = 3,
    val progressText: String = "",
    val guideStory: String,
    val dialog: Int,
    val completeFirstGuide: Boolean = false
)
