package com.djhb.petopia.presentation.gallery

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.remote.GalleryRepository
import com.djhb.petopia.data.remote.GalleryRepositoryImpl
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//갤러리와 포토의 공유 뷰모델
class GallerySharedViewModel(private val galleryRepository: GalleryRepository) :
    ViewModel() {
    private val user = LoginData.loginUser

    //갤러리 리스트
    private val _galleryListLiveData = MutableLiveData<List<GalleryModel>>(listOf())
    val galleryListLiveData: LiveData<List<GalleryModel>> = _galleryListLiveData
    private var galleryList: MutableList<GalleryModel> =
        _galleryListLiveData.value?.toMutableList() ?: mutableListOf()

    //레이아웃모드 : "ADD" 추가, "EDIT" 편집, "READ" 읽기전용(기본값)
    private val _layoutModeLiveData = MutableLiveData("READ")
    val layoutModeLiveData: LiveData<String> = _layoutModeLiveData

    //현재 보고있는 사진 : uri.toString() 갤러리 사진 단일 또는 다수
    private val _currentPhotoListLiveData = MutableLiveData<GalleryModel>()
    val currentPhotoLiveData: LiveData<GalleryModel> = _currentPhotoListLiveData


    //새로운 사진 : 임시 저장 데이터, 완료시 선택된 사진의 값으로 교체된다.
    private val _newPhotoListLiveData = MutableLiveData<List<Uri>>()
    val newPhotoListLiveData: LiveData<List<Uri>> = _newPhotoListLiveData
    private lateinit var newPhotoList: GalleryModel


    //삭제모드 : "REMOVE" 삭제, "COMPLETE" 완료(기본값)
    private val _removeModeLiveData = MutableLiveData("COMPLETE")
    val removeModeLiveData: LiveData<String> = _removeModeLiveData

    //삭제할 사진 : 임시 저장 데이터, 완료시 삭제된다.
    var removePhotoList = mutableListOf<GalleryModel>()

    //삭제모드 : "REMOVE" 삭제, "COMPLETE" 완료(기본값)
    private val _checkedPhotoLiveData = MutableLiveData<Int>()
    val checkedPhotoLiveData: LiveData<Int> = _checkedPhotoLiveData

    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing get() = _isProcessing


    //데이터베이스에서 사용자의 갤러리를 불러오는 함수
    fun loadGalleryList() {
        viewModelScope.launch {
            _isProcessing.value = true
            val list = async {
                galleryRepository.selectGalleryList(user)
            }
            val successList = list.await()

            val imageUris = mutableListOf<StorageReference?>()

            for (galleryModel in successList) {
                galleryModel.imageUris.clear()
                imageUris.add(galleryRepository.selectGalleryMainImages(galleryModel.uid))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
                if (uri != null)
                    successList[uriIndex].imageUris.add(galleryRepository.selectDownloadUri(uri))
            }

// _galleryListLiveData.value = galleryRepository.selectGalleryMainImages(successList).toList()
            _galleryListLiveData.value = successList.toList()
            _isProcessing.value = false
        }
    }

    //등록한 사진을 저장하는 함수
    private fun saveGalleryList() {
        viewModelScope.launch {
            _currentPhotoListLiveData.value?.let { galleryRepository.createGallery(it) }
        }
    }


    //진입경로에 따라 레이아웃 모드를 변경해주는 함수
    fun changeLayoutMode(layoutMode: String) {
        _layoutModeLiveData.value = layoutMode
        when (layoutMode) {
            "ADD" -> {
                newPhotoList = GalleryModel(
                    "", writer = user, imageUris = mutableListOf()
                )
                _currentPhotoListLiveData.value = newPhotoList
            }

            "EDIT" -> newPhotoList = _currentPhotoListLiveData.value ?: GalleryModel(
                "", writer = user
            )
        }

    }

    //선택된 사진을 상세페이지로 보여주기 위해 담거나, 삭제모드일 경우 임시 저장 변수에 담아주는 함수
    fun updateGalleryList(photoList: GalleryModel, position: Int) {
        when (_removeModeLiveData.value) {
            "REMOVE" -> {
                if (removePhotoList[position].checked) {
                    removePhotoList[position] = photoList.copy(checked = false)
                    _checkedPhotoLiveData.value = position
                } else {
                    removePhotoList[position] = photoList.copy(checked = true)
                    _checkedPhotoLiveData.value = position
                }
            }

            "COMPLETE" -> _currentPhotoListLiveData.value = photoList.copy(index = position)
        }

    }

    //등록 또는 변경될 가능성이 있는 새로운 사진을 담는 함수
    fun considerNewPhoto(uriList: List<Uri>) {
        val photoList = mutableListOf<String>()
        uriList.forEach { photoList.add(it.toString()) }
        newPhotoList = newPhotoList.copy(imageUris = photoList)
    }

    //등록 또는 변경될 가능성이 있는 새로운 사진의 날짜를 담는 함수
    fun considerNewPhotoDate(dateTime: String) {
        newPhotoList = newPhotoList.copy(
            photoDate = dateTime
        )
    }

    //등록 또는 변경될 가능성이 있는 사진의 제목을 담는 함수
    fun considerNewPhotoTitle(editText: String) {
        newPhotoList = newPhotoList.copy(
            titleText = editText
        )
    }

    fun prepared(): Boolean {
        return newPhotoList.imageUris.isNotEmpty() && newPhotoList.titleText.isNotEmpty()
    }

    //현재 사진을 새 사진으로 교체하는 함수
    fun updateNewGallery(index: Int) {
        galleryList = _galleryListLiveData.value?.toMutableList() ?: mutableListOf()

        when (_layoutModeLiveData.value) {
            "ADD" -> {
                _currentPhotoListLiveData.value =
                    newPhotoList
                galleryList.add(0, _currentPhotoListLiveData.value!!)
                saveGalleryList()
            }

            "EDIT" -> {
                _currentPhotoListLiveData.value = newPhotoList
                galleryList[index] = _currentPhotoListLiveData.value!!
            }
        }
        _galleryListLiveData.value = galleryList

    }

    //삭제 모드를 변경해주는 함수
    fun changeRemoveMode() {
        _removeModeLiveData.value =
            if (_removeModeLiveData.value == "COMPLETE") "REMOVE"
            else "COMPLETE"
    }

    fun cancelRemoveMode() {
        removePhotoList.clear()
        removePhotoList = _galleryListLiveData.value!!.toMutableList()
        changeRemoveMode()
    }


    //사진 삭제가 반영된 리스트로 교체하는 함수
    fun updateRemoveGalleryList(mode: String) {
        when (mode) {
            "REMOVE" -> {
                removePhotoList = _galleryListLiveData.value!!.toMutableList()
            }

            "COMPLETE" -> {
                var list = removePhotoList.filter { it.checked }


                viewModelScope.launch {
                    for (i in list) {
                        galleryRepository.deleteGallery(i.uid)
                    }
                }
                removePhotoList.removeIf { it.checked }

// removePhotoList.forEach { removePhoto ->
// galleryList.removeIf { it.uId == removePhoto.uId }
// }

                _galleryListLiveData.value = removePhotoList


            }
        }
    }
}


class GallerySharedViewModelFactory : ViewModelProvider.Factory {
    private val repository =
        GalleryRepositoryImpl()

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return GallerySharedViewModel(
            repository
        ) as T
    }
}