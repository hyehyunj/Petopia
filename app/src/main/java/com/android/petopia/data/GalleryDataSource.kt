package com.android.petopia.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken





class GalleryDataSource {

    companion object {
        private var INSTANCE : GalleryDataSource ?= null
        fun getGalleryDataSource() : GalleryDataSource {
            return synchronized(GalleryDataSource::class) {
                val newInstance = INSTANCE ?: GalleryDataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }

    //sharedpreferences에 갤러리를 저장하는 함수
    fun saveGalleryList(context: Context, galleryList: List<GalleryModel>) {
        val pref = context.getSharedPreferences("pref", 0)
        val edit = pref.edit()
        val jsonString = Gson().toJson(galleryList)
        edit.putString("gallery", jsonString)
        edit.apply()
    }

    //sharedpreferences에 저장된 갤러리를 불러오는 함수
    fun loadGalleryList(context: Context): List<GalleryModel> {
        val pref = context.getSharedPreferences("pref", 0)
        val jsonString = pref.getString("gallery", "")
        return if (jsonString != "") {
            val type = object : TypeToken<List<GalleryModel>>() {}.type
            Gson().fromJson(jsonString, type)
        } else {
            listOf()
        }
    }
}



