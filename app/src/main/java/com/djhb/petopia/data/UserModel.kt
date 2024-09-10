package com.djhb.petopia.data

import android.os.Parcelable
import com.djhb.petopia.presentation.community.Authority
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String = "",
    var password: String = "",
    var name: String = "",
    var nickname: String = "",
    var email: String = "",
    var reportList : List<ReportModel>? = null,
    var pet: PetModel? = null,
    var completedGuide: Boolean = false,
    val createdDate: Long = System.currentTimeMillis(),
    var updatedDate: Long? = null,
    var authority: Authority = Authority.CLIENT
) : Parcelable
