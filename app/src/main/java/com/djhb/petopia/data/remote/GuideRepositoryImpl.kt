package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel


class GuideRepositoryImpl(private val guideLocalDataSource: GuideLocalDataSource) :
    GuideRepository {


    override fun getGuideData(pageNumber: Int): GuideModel {
        return guideLocalDataSource.getGuideData(pageNumber)
    }
    override fun getPetListData(breed: String): List<String> {
        return guideLocalDataSource.getPetListData(breed)
    }

}