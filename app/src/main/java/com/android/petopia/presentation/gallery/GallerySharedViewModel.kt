package com.android.petopia.presentation.gallery

import android.net.Uri
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.GalleryModel
import com.android.petopia.data.UserModel

//갤러리와 포토의 공유 뷰모델
//class GallerySharedViewModel(private val galleryRepository: GalleryRepository) :
//    ViewModel() {
class GallerySharedViewModel :
    ViewModel() {

    //갤러리 리스트
    private val _galleryListLiveData = MutableLiveData<List<GalleryModel>>(listOf())
    val galleryListLiveData: LiveData<List<GalleryModel>> = _galleryListLiveData
    private val galleryList: MutableList<GalleryModel> =
        _galleryListLiveData.value?.toMutableList() ?: mutableListOf()

    //레이아웃모드 : "ADD" 추가, "EDIT" 편집, "READ" 읽기전용(기본값)
    private val _layoutModeLiveData = MutableLiveData("READ")
    val layoutModeLiveData: LiveData<String> = _layoutModeLiveData

    //현재 보고있는 사진 : uri.toString() 갤러리 사진 단일 또는 다수
    private val _currentPhotoListLiveData = MutableLiveData<GalleryModel>()
    val currentPhotoLiveData: LiveData<GalleryModel> = _currentPhotoListLiveData

    //새로운 사진 : 임시 저장 데이터, 완료시 선택된 사진의 값으로 교체된다.
    private lateinit var newPhotoList: GalleryModel

    //삭제모드 : "REMOVE" 삭제, "COMPLETE" 완료(기본값)
    private val _removeModeLiveData = MutableLiveData("COMPLETE")
    val removeModeLiveData: LiveData<String> = _removeModeLiveData

    //삭제할 사진 : 임시 저장 데이터, 완료시 삭제된다.
    private val removePhotoList = mutableListOf<GalleryModel>()


    //진입경로에 따라 레이아웃 모드를 변경해주는 함수
    fun changeLayoutMode(layoutMode: String) {
        _layoutModeLiveData.value = layoutMode
        if (layoutMode == "ADD") {
            newPhotoList = GalleryModel(
                "", UserModel(), 0, 0, imageUris = mutableListOf()
            )
            _currentPhotoListLiveData.value = newPhotoList
        }
    }

    //선택된 사진을 상세페이지로 보여주기 위해 담거나, 삭제모드일 경우 임시 저장 변수에 담아주는 함수
    fun updateGalleryList(photoList: GalleryModel, position: Int) {

        when (_removeModeLiveData.value) {
            "REMOVE" -> {
                if (!photoList.checked) removePhotoList += photoList.copy(checked = false) else removePhotoList -= photoList.copy(
                checked = true
            )}
            "COMPLETE" -> _currentPhotoListLiveData.value = photoList.copy(index = position)
        }
    }

    //등록 또는 변경될 가능성이 있는 새로운 사진의 정보를 담는 함수
    fun considerNewPhoto(uriList: List<Uri>) {
        val photoList = mutableListOf<String>()
        uriList.forEach { photoList.add(it.toString()) }
        when (_layoutModeLiveData.value) {
            "ADD" -> newPhotoList = newPhotoList.copy(imageUris = photoList)
            "EDIT" -> newPhotoList =
                _currentPhotoListLiveData.value?.copy(
                    imageUris = photoList
                ) ?: GalleryModel(
                    "", UserModel(), 0, 0, imageUris = photoList
                )
        }
    }

    //현재 사진을 새 사진으로 교체하는 함수
    fun updateNewGallery(titleText: String, date: String, index: Int) {
        when (_layoutModeLiveData.value) {
            "ADD" -> {
                _currentPhotoListLiveData.value =
                    newPhotoList.copy(
                        titleText = titleText,
//                        date = date
                    )
                galleryList.add(0, _currentPhotoListLiveData.value!!)
            }

            "EDIT" -> {
                _currentPhotoListLiveData.value = newPhotoList.copy(
                    titleText = titleText,
//                    date = date
                )
                galleryList[index] = _currentPhotoListLiveData.value!!

            }
        }
        _galleryListLiveData.value = galleryList
    }

    //삭제 모드를 변경해주는 함수
    fun changeRemoveMode() {
        _removeModeLiveData.value =
            if (_removeModeLiveData.value == "COMPLETE") "REMOVE" else "COMPLETE"
    }

    //사진 삭제가 반영된 리스트로 교체하는 함수
    fun updateRemovedGalleryList() {

        removePhotoList.forEach { removePhoto ->
            galleryList.removeIf { it.uId == removePhoto.uId }
        }
        _galleryListLiveData.value = galleryList
    }


}


//class GallerySharedViewModelFactory(context: Context) : ViewModelProvider.Factory {
//    private val repository =
//        GalleryRepositoryImpl()
//
//    override fun <T : ViewModel> create(
//        modelClass: Class<T>,
//        extras: CreationExtras
//    ): T {
//        return GallerySharedViewModel(
//            repository
//        ) as T
//    }
//}