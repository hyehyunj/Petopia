package com.android.petopia.presentation.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.petopia.data.GalleryModel

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


    //모드 : "ADD" 추가, "EDIT" 편집, "READ" 읽기전용(기본값)
    private val _layoutModeLiveData = MutableLiveData("READ")
    val layoutModeLiveData: LiveData<String> = _layoutModeLiveData

    //선택된 사진 : 현재 보고있는 사진
    private val _currentPhotoLiveData = MutableLiveData<GalleryModel>()
    val currentPhotoLiveData: LiveData<GalleryModel> = _currentPhotoLiveData

    //새로운 사진 : 임시 사진 데이터, 완료시 선택된 사진의 값으로 교체된다.
    private lateinit var newPhoto: GalleryModel

    //삭제할 사진 : 임시 사진 휴지통, 완료시 휴지통의 사진들을 제거한다.
    private lateinit var removePhoto: List<GalleryModel>


    //진입경로에 따라 레이아웃 모드를 변경해주는 함수
    fun changeLayoutMode(layoutMode: String) {
        _layoutModeLiveData.value = layoutMode
    }

    //선택된 사진을 업데이트해주는 함수
    fun updateCurrentPhoto(photo: GalleryModel, position: Int) {
        _currentPhotoLiveData.value = photo.copy(index = position)
    }

    //등록 또는 변경될 가능성이 있는 새로운 사진의 정보를 담는 함수
    fun considerNewPhoto(titleImage: String) {
        newPhoto = GalleryModel(
                    titleImage = titleImage,
                    "",
                    ""
                )
        if(_layoutModeLiveData.value == "EDIT") newPhoto =
            _currentPhotoLiveData.value?.copy(
                titleImage = titleImage) ?: GalleryModel(
                titleImage = titleImage,
                "",
                "",
                _currentPhotoLiveData.value?.index ?: 0

            )


    }

    //현재 사진을 새 사진으로 교체하는 함수
    fun updateNewGallery(titleText: String, date: String, index: Int) {
        when (_layoutModeLiveData.value) {
            "ADD" -> {
                _currentPhotoLiveData.value =
                    newPhoto.copy(
                        titleText = titleText,
                        date = date
                    )
                galleryList.add(0,_currentPhotoLiveData.value!!)
            }

            "EDIT" -> {
                _currentPhotoLiveData.value = newPhoto.copy(
                    titleText = titleText,
                    date = date
                )
                galleryList[index] = _currentPhotoLiveData.value!!

            }
        }
        _galleryListLiveData.value = galleryList
    }
//        _currentPhotoLiveData.value = newPhoto?.copy(titleText = titleText, date = date)
//        val index = galleryList.indexOf(galleryList.find{ it.index == _currentPhotoLiveData.value?.index })
//        if(index == -1) _currentPhotoLiveData.value?.let { galleryList.add(it) }
//        else galleryList[index] = _currentPhotoLiveData.value!!
//        _galleryListLiveData.value = galleryList


    //삭제될 가능성이 있는 사진들을 임시 휴지통에 담는 함수
    fun considerRemovePhoto(photo: GalleryModel, checked: Boolean) {
        if (checked) removePhoto += photo else removePhoto -= photo
    }

    //사진 삭제가 반영된 리스트로 교체하는 함수
    fun updateRemovedGallery() {
        removePhoto.forEach { removePhoto ->
            galleryList.removeIf { it.index == removePhoto.index }
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
