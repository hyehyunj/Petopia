package com.djhb.petopia.presentation.admin.post

import com.djhb.petopia.R

data class Card(
    val name: String,
    val color: Int
)

fun Any?.genCards() : List<Card> = listOf(
    Card("Blue Card", R.drawable.img_chihuahua),
    Card("Red Card", R.drawable.img_americanshoerthair),
    Card("Green Card", R.drawable.img_shiba),
    Card("Yellow Card", R.drawable.img_maltese)
)