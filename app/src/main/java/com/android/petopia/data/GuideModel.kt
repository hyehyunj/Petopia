package com.android.petopia.data

import android.util.Pair

data class GuideModel(
    val progressBar : Int,
    val progressText : String,
    val guideStory : String,
    val dialog : Int,
    val completeFirstGuide : Boolean = false
)

data class PetModel(
    val petName : String,
    val petAppearance : String,
    val petRelation : Int
)

