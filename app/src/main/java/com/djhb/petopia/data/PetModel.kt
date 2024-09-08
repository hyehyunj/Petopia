package com.djhb.petopia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class PetModel(
    val petName : String,
    val petAppearance : PetAppearance,
    val petRelation : PetRelation,
    val id: String = "",
    val createdDate : Long = System.currentTimeMillis(),
    var updatedDate: Long = System.currentTimeMillis(),
    var uid: String = UUID.randomUUID().toString()
) : Parcelable
