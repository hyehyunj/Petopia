package com.djhb.petopia.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID
@Parcelize
data class DDayModel(
    val date: String = "",
    val name: String = "",
    val uid: String = UUID.randomUUID().toString()
) : Parcelable

