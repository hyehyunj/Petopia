package com.djhb.petopia.presentation.album

import android.net.Uri
import androidx.core.net.toUri
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
class AlbumSharedViewModel(private val albumRepository: GalleryRepository) :
    ViewModel() {
    private val user = LoginData.loginUser

    //앨범 리스트
    private val _albumListLiveData = MutableLiveData<List<GalleryModel>>(listOf())
    val albumListLiveData: LiveData<List<GalleryModel>> = _albumListLiveData
    private var albumList: MutableList<GalleryModel> =
        _albumListLiveData.value?.toMutableList() ?: mutableListOf()

    //레이아웃모드 : "ADD" 추가, "EDIT" 편집, "READ" 읽기전용(기본값)
    private val _layoutModeLiveData = MutableLiveData("READ")
    val layoutModeLiveData: LiveData<String> = _layoutModeLiveData

    //현재 보고있는 사진 : uri.toString() 앨범 사진 단일 또는 다수
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


    //데이터베이스에서 사용자의 앨범 리스트를 불러오는 함수
    fun loadGalleryList() {
        viewModelScope.launch {
            _isProcessing.value = true
            val list = async {
                albumRepository.selectGalleryList(user)
            }
            val successList = list.await()

            val imageUris = mutableListOf<StorageReference?>()

            for (albumModel in successList) {
                albumModel.imageUris.clear()
                imageUris.add(albumRepository.selectGalleryMainImages(albumModel.uid))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
                if (uri != null)
                    successList[uriIndex].imageUris.add(albumRepository.selectDownloadUri(uri))
            }

// _albumListLiveData.value = albumRepository.selectGalleryMainImages(successList).toList()
            _albumListLiveData.value = successList.toList()
            _isProcessing.value = false
        }
    }

    //추가 또는 편집한 사진을 데이터베이스에 저장하는 함수
    private fun saveGalleryList() {
        viewModelScope.launch {
            _currentPhotoListLiveData.value?.let { albumRepository.createGallery(it) }
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

    //사진 리스트를 편집 또는 추가하기 위해 준비해주는 함수
    fun preparePhotoList() {
        val stringToUri = mutableListOf<Uri>()
        if(_layoutModeLiveData.value == "EDIT") {
            _currentPhotoListLiveData.value?.imageUris?.forEach {
                stringToUri.add(it.toUri())
        } }
        _newPhotoListLiveData.value = stringToUri
    }

    //등록 또는 변경될 가능성이 있는 새로운 사진을 담는 함수
    fun considerNewPhoto(uriList: List<Uri>) {
        //편집화면에 보여주기 위해 담는 리스트
        _newPhotoListLiveData.value = uriList
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

    //사진을 추가하거나 편집하기 위한 준비를 마쳤는지 확인하는 함수
    fun checkPrepared(): Boolean {
        return newPhotoList.imageUris.isNotEmpty() && newPhotoList.titleText.isNotEmpty()
    }

    //현재 사진을 새 사진으로 교체하는 함수
    fun updateNewGallery(index: Int) {
        albumList = _albumListLiveData.value?.toMutableList() ?: mutableListOf()

        when (_layoutModeLiveData.value) {
            "ADD" -> {
                _currentPhotoListLiveData.value =
                    newPhotoList
                albumList.add(0, _currentPhotoListLiveData.value!!)
                saveGalleryList()
            }

            "EDIT" -> {
                _currentPhotoListLiveData.value = newPhotoList
                albumList[index] = _currentPhotoListLiveData.value!!
            }
        }
        _albumListLiveData.value = albumList

    }

    //삭제 모드를 변경해주는 함수
    fun changeRemoveMode() {
        _removeModeLiveData.value =
            if (_removeModeLiveData.value == "COMPLETE") "REMOVE"
            else "COMPLETE"
    }

    fun cancelRemoveMode() {
        removePhotoList.clear()
        removePhotoList = _albumListLiveData.value!!.toMutableList()
        changeRemoveMode()
    }


    //사진 삭제가 반영된 리스트로 교체하는 함수
    fun updateRemoveGalleryList(mode: String) {
        when (mode) {
            "REMOVE" -> {
                removePhotoList = _albumListLiveData.value!!.toMutableList()
            }

            "COMPLETE" -> {
                var list = removePhotoList.filter { it.checked }


                viewModelScope.launch {
                    for (i in list) {
                        albumRepository.deleteGallery(i.uid)
                    }
                }
                removePhotoList.removeIf { it.checked }

// removePhotoList.forEach { removePhoto ->
// albumList.removeIf { it.uId == removePhoto.uId }
// }

                _albumListLiveData.value = removePhotoList


            }
        }
    }
}


class AlbumSharedViewModelFactory : ViewModelProvider.Factory {
    private val repository =
        GalleryRepositoryImpl()

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return AlbumSharedViewModel(
            repository
        ) as T
    }
}