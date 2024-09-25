package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.IntroModel

interface IntroRepository {
    fun getIntroData() : List<IntroModel>
    fun updateIntroSkip(context: Context, introSkip: Boolean) : Boolean
    fun loadIntroSkip(context: Context) : Boolean
}