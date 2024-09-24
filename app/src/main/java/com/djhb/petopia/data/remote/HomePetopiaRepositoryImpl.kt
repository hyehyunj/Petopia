package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.AlarmLocalDataSource
import com.djhb.petopia.data.DDayModel
import com.djhb.petopia.data.HomePetopiaLocalDataSource
import com.djhb.petopia.data.PetRelation

class HomePetopiaRepositoryImpl(private val homePetopiaLocalDataResource: HomePetopiaLocalDataSource) :
    HomePetopiaRepository {
    override fun getPetMassage(petRelation: PetRelation): String {
        return homePetopiaLocalDataResource.getPetMassage(petRelation)
    }

}