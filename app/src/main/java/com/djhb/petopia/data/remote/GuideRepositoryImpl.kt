package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetAppearanceModel


class GuideRepositoryImpl(private val guideLocalDataSource: GuideLocalDataSource) :
    GuideRepository {


    override fun getGuideData(pageNumber: Int): GuideModel {
        return guideLocalDataSource.getGuideData(pageNumber)
    }

    override fun getPetListData(breed: String): List<PetAppearanceModel> {
        return guideLocalDataSource.getPetListData(breed)
    }

    override fun updateStatusData(pageNumber: Int, status: String): GuideModel {
        return guideLocalDataSource.updateStatusData(pageNumber,status)
    }

}