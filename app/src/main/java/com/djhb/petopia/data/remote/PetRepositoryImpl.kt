package com.djhb.petopia.data.remote

import com.djhb.petopia.data.GuideModel
import com.djhb.petopia.data.PetLocalDatasource
import com.djhb.petopia.data.PetModel


class PetRepositoryImpl(private val petLocalDataSource: PetLocalDatasource) :
    PetRepository {


    override fun getPetData(): PetModel {
        return petLocalDataSource.getPetData()
    }

    override fun setPetNameData(inputName: String): MutableList<String> {
        return petLocalDataSource.setPetNameData(inputName)
    }

    override fun setPetAppearanceData(selectedAppearance: String): MutableList<String> {
        return petLocalDataSource.setPetAppearanceData(selectedAppearance)
    }

    override fun setPetRelationData(selectedRelation: String): MutableList<String> {
        return petLocalDataSource.setPetRelationData(selectedRelation)
    }

    override fun checkPetData(index: Int): Boolean {
        return petLocalDataSource.checkPetData(index)
    }
}