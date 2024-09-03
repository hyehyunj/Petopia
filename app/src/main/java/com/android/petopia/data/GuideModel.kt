package com.android.petopia.data

data class GuideModel(
    val progressBar : Int,
    val progressText : String,
    val guideStory : String,
    val dialog : Int,
    val completeFirstGuide : Boolean = false
)


