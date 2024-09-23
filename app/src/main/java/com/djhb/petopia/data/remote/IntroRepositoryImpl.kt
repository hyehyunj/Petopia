package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideLocalDataSource
import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.IntroLocalDataSource
import com.djhb.petopia.data.IntroModel
import com.djhb.petopia.data.PetAppearanceModel


class IntroRepositoryImpl(private val introLocalDataSource: IntroLocalDataSource) :
    IntroRepository {
    override fun getIntroData(): List<IntroModel> {
        return introLocalDataSource.getIntroData()
    }


}