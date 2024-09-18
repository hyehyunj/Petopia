package com.djhb.petopia.data.remote

import com.djhb.petopia.data.LetterModel
import com.djhb.petopia.data.UserModel
import com.google.firebase.firestore.DocumentSnapshot

interface LetterRepository {

    suspend fun createLetter(letterModel: LetterModel)
    suspend fun selectInitLetterList(user: UserModel): List<DocumentSnapshot>
    suspend fun selectLetterList(user: UserModel): MutableList<LetterModel>
    suspend fun updateLetter(letterModel: LetterModel)
    suspend fun deleteLetter(key: String)
    fun convertToLetterModel(documents: List<DocumentSnapshot>): MutableList<LetterModel>

}