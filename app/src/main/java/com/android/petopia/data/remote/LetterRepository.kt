package com.android.petopia.data.remote

import com.android.petopia.data.LetterModel
import com.android.petopia.data.UserModel

interface LetterRepository {

    suspend fun createLetter(letterModel: LetterModel)
    suspend fun selectLetterList(user: UserModel): MutableList<LetterModel>
    suspend fun updateLetter(letterModel: LetterModel)
    suspend fun deleteLetter(key: String)

}