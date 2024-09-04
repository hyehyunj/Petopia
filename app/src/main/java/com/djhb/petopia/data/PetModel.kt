package com.djhb.petopia.data

import java.util.UUID

data class PetModel(
    val petName : String,
    val petAppearance : String,
    val petRelation : Int, //enum
    val id: String = "",
    val createdDate : Long,
    var updatedDate: Long,
    val uId: String = UUID.randomUUID().toString()
)
