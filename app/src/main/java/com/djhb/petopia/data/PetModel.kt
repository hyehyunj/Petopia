package com.djhb.petopia.data

import java.util.UUID

data class PetModel(
    val petName : String,
    val petAppearance : PetAppearance,
    val petRelation : PetRelation,
    val id: String = "",
    val createdDate : Long,
    var updatedDate: Long,
    val uId: String = UUID.randomUUID().toString()
)
