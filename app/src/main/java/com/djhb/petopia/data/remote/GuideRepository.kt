package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetAppearanceModel

interface GuideRepository {
    fun getGuideData(pageNumber : Int) : GuideModel
    fun getPetListData(breed : String) : List<PetAppearanceModel>
fun updateStatusData(pageNumber : Int, status: String) : GuideModel
}