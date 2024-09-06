package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideModel

interface GuideRepository {
    fun getGuideData(pageNumber : Int) : GuideModel
    fun getPetListData(breed : String) : List<String>

}