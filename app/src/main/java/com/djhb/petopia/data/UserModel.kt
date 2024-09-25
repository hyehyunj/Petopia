package com.djhb.petopia.data

import android.os.Parcelable
import com.djhb.petopia.presentation.community.Authority
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String = "",
    var password: String? = "",
    var nickname: String = "",
    var email: String = "",
    var reportList : MutableList<String> = mutableListOf(),
    var pet: PetModel? = null,
    var dday: DDayModel? = null,
    var completedGuide: Boolean = false,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null,
    var authority: Authority = Authority.CLIENT
) : Parcelable
