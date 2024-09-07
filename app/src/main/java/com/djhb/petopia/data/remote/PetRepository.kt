package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetModel


interface PetRepository {
    fun getPetData(): PetModel
    fun setPetNameData(inputName: String): MutableList<String>
    fun setPetAppearanceData(selectedAppearance: String): MutableList<String>
    fun setPetRelationData(selectedRelation: String): MutableList<String>
    fun checkPetData(index: Int): Boolean
}