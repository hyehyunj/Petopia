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
    val createdDate : Long,
    var updatedDate: Long,
    val uId: String = UUID.randomUUID().toString()
) : Parcelable
