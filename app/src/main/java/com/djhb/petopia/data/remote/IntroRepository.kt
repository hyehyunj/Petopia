package com.djhb.petopia.data.remote

import com.djhb.petopia.data.IntroModel

interface IntroRepository {
    fun getIntroData() : List<IntroModel>
}