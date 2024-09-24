package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.PetRelation

interface HomePetopiaRepository {
fun getPetMassage(petRelation: PetRelation) : String
}