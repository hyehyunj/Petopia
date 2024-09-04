package com.djhb.petopia.presentation.gallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.GalleryModel

//class GalleryViewModel (private val galleryRepository: GalleryRepository) :
//        ViewModel() {
//
//            //갤러리 리스트
//        private val _galleryListLiveData = MutableLiveData<List<GalleryModel>>()
//        val galleryListLiveData: LiveData<List<GalleryModel>> = _galleryListLiveData
//        private val galleryList: MutableList<GalleryModel> = _galleryListLiveData.value?.toMutableList() ?: mutableListOf()
//
//        //갤러리 불러오는 함수
//        fun getGalleryList(context: Context) {
//            galleryRepository.updateGalleryList(context, _galleryListLiveData.value ?: listOf())
//                    }
//
//    }
//
//
//
//
//
//
//    class GalleryViewModelFactory(context: Context) : ViewModelProvider.Factory {
//
//        private val repository = GalleryRepositoryImpl()
//        override fun <T : ViewModel> create(
//            modelClass: Class<T>,
//            extras: CreationExtras
//        ): T {
//
//            return GalleryViewModel(
//                repository
//            ) as T
//        }
//    }
