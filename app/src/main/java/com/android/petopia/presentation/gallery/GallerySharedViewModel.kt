package com.android.petopia.presentation.gallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.android.petopia.data.GalleryDataSource
import com.android.petopia.data.GalleryModel
import com.android.petopia.data.GalleryRepositoryImpl

class GallerySharedViewModel (private val galleryRepository: GalleryRepository) :
        ViewModel() {

    //모드 : "ADD" 추가, "EDIT" 편집, "" 읽기전용(기본값)
    private val _layoutModeLiveData = MutableLiveData("")
    val layoutModeLiveData: LiveData<String> = _layoutModeLiveData

    //선택된 사진 : 현재 보고있는 사진
    private val _currentPhotoLiveData = MutableLiveData<GalleryModel>()
    val currentPhotoLiveData: LiveData<GalleryModel> = _currentPhotoLiveData
    val newPhoto: GalleryModel? = _currentPhotoLiveData.value

    //진입경로에 따라 레이아웃 모드를 변경해주는 함수
    fun changeLayoutMode(layoutMode: String) {
        _layoutModeLiveData.value = layoutMode
    }

    //선택된 사진을 업데이트해주는 함수
    fun updateCurrentPhoto(photo: GalleryModel) {
        _currentPhotoLiveData.value = photo
    }

    fun considerNewPhoto(titleImage: String) {
        newPhoto?.copy(titleImage = titleImage)
    }

    fun updateNewPhoto(titleText: String, date: String) {

        newPhoto?.copy(titleText = titleText, date = date)
        _currentPhotoLiveData.value = newPhoto!!
    }
}



    class GallerySharedViewModelFactory(context: Context) : ViewModelProvider.Factory {

        private val repository = GalleryRepositoryImpl(GalleryDataSource.getGalleryDataSource().loadGalleryList(context))
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {

            return GallerySharedViewModel(
                repository
            ) as T
        }
    }
