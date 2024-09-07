package com.djhb.petopia.data

object PetLocalDatasource {
private val makingPetData = mutableListOf("", "", "")

    fun setPetNameData(
        inputName: String
    ): MutableList<String> {
        makingPetData[0] = inputName
      return makingPetData
    }

    fun setPetAppearanceData(
        selectedAppearance: String
    ): MutableList<String> {
        makingPetData[1] = selectedAppearance
        return makingPetData
    }

    fun setPetRelationData(
        selectedRelation: String
    ): MutableList<String> {
        makingPetData[2] = selectedRelation
        return makingPetData
    }


    fun checkPetData(index: Int): Boolean {
       return makingPetData[index] != ""
    }



    fun getPetData(
    ): PetModel {
        var appearance = PetAppearance.CHIHUAHUA
        var relation = PetRelation.FRIEND




        when (makingPetData[1]) {
            "말티즈" -> appearance = PetAppearance.MALTESE
            "푸들" -> appearance = PetAppearance.POODLE
            "치와와" -> appearance = PetAppearance.CHIHUAHUA
            "포메라니안" -> appearance = PetAppearance.POMERANIAN
            "웰시코기" -> appearance = PetAppearance.WELSHCORGI
            "시츄" -> appearance = PetAppearance.SHIHTZU
            "시바" -> appearance = PetAppearance.SHIBA
            "비숑" -> appearance = PetAppearance.BICHON
            "리트리버" -> appearance = PetAppearance.RETRIEVER
            "코리안숏헤어" -> appearance = PetAppearance.KOREANSHORTHAIR
            "페르시안" -> appearance = PetAppearance.PERSIAN
            "터키쉬앙고라" -> appearance = PetAppearance.TURKISHANGORA
            "샴" -> appearance = PetAppearance.SIAMESE
            "노르웨이숲" -> appearance = PetAppearance.NORWEGIANFOREST
            "러시안블루" -> appearance = PetAppearance.RUSSIANBLUE
            "스코티쉬폴드" -> appearance = PetAppearance.SCOTTISHFOLD
            "아비시니안" -> appearance = PetAppearance.ABYSSINIAN
            "아메리칸숏헤어" -> appearance = PetAppearance.AMERICANSHORTHAIR
        }

        when (makingPetData[2]) {
            "CHILD" -> relation = PetRelation.CHILD
            "YOUNGER" -> relation = PetRelation.YOUNGER
            "FRIEND" -> relation = PetRelation.FRIEND
        }


        val petModel = PetModel(
            makingPetData[0],
            appearance,
            relation,
            createdDate = System.currentTimeMillis(),
            updatedDate = System.currentTimeMillis()
        )


        return petModel
    }
}