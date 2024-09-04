package com.djhb.petopia.network

import com.google.firebase.Firebase
import com.google.firebase.database.database

object FirebaseReference {
    val reference = Firebase.database("https://petopia-92b56-default-rtdb.asia-southeast1.firebasedatabase.app").reference
}