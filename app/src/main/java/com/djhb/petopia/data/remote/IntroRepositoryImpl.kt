package com.djhb.petopia.data.remote

import android.content.Context
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

    override fun updateIntroSkip(context: Context, introSkip: Boolean): Boolean {
        return introLocalDataSource.updateSkipIntroData(context, introSkip)
    }

    override fun loadIntroSkip(context: Context): Boolean {
        return introLocalDataSource.loadSkipIntroData(context)
    }


}