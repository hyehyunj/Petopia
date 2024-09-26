package com.djhb.petopia.presentation.album

import android.net.Uri
import android.util.Log
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    //현재 보고있는 사진 : 앨범 사진(단일 또는 다수)
    private val _currentAlbumLiveData = MutableLiveData<GalleryModel>()
    val currentAlbumLiveData: LiveData<GalleryModel> = _currentAlbumLiveData

    //새로운 사진 : 임시 저장 데이터. 완료시 이 변수의 값으로 추가 및 편집된다.
    private val _newUriListLiveData = MutableLiveData<List<Uri>>()
    val newUriListLiveData: LiveData<List<Uri>> = _newUriListLiveData
    private lateinit var newUriList: GalleryModel
    var cropPosition : Int = 0

    //삭제모드 : "REMOVE" 삭제, "COMPLETE" 완료(기본값)
    private val _removeModeLiveData = MutableLiveData("COMPLETE")
    val removeModeLiveData: LiveData<String> = _removeModeLiveData

    //삭제할 사진 : 임시 저장 데이터. 완료시 삭제된다.
    private val _checkedAlbumLiveData = MutableLiveData<Int>()
    val checkedAlbumLiveData: LiveData<Int> = _checkedAlbumLiveData
    var removeAlbumList = mutableListOf<GalleryModel>()

    //로딩 확인
    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing get() = _isProcessing

    //데이터베이스 관련 함수
    //데이터베이스에서 사용자의 앨범 리스트를 불러오는 함수
    fun loadAlbumList() {
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
            _albumListLiveData.value = successList.toList()
            sortAlbumList()
            _isProcessing.value = false
        }
    }

    //데이터베이스에서 사용자의 Uri 리스트를 불러오는 함수
    fun loadUriList(galleryModel: GalleryModel) {
        viewModelScope.launch {
            val snapshots = albumRepository.selectAllGalleryImages(galleryModel)
            galleryModel.imageUris.clear()
            for (snapshot in snapshots) {
                galleryModel.imageUris.add(albumRepository.selectDownloadUri(snapshot))
            }
            _currentAlbumLiveData.value = galleryModel
        }
    }

    //추가 또는 편집한 사진을 데이터베이스에 저장하는 함수
    private fun saveAlbumList() {
        viewModelScope.launch {
            _currentAlbumLiveData.value?.let { albumRepository.createGallery(it) }
        }
    }

    //추가 및 편집 관련 함수
    //진입경로에 따라 레이아웃 모드를 변경해주는 함수
    fun changeLayoutMode(layoutMode: String) {
        _layoutModeLiveData.value = layoutMode
        when (layoutMode) {
            "ADD" -> {
                newUriList = GalleryModel(
                    "", writer = user, imageUris = mutableListOf()
                )
                _currentAlbumLiveData.value = newUriList
            }

            "EDIT"-> newUriList = _currentAlbumLiveData.value ?: GalleryModel(
                "", writer = user
            )
        }
    }

    //사진 리스트를 추가 또는 편집하기 위해 준비해주는 함수
    fun prepareNewAlbumList() {
        _newUriListLiveData.value = listOf()
//        val stringToUri = mutableListOf<Uri>()
//        if (_layoutModeLiveData.value == "EDIT") {
//            _currentAlbumLiveData.value?.imageUris?.forEach {
//                stringToUri.add(it.toUri())
//            }
//        }
//        _newUriListLiveData.value = stringToUri
    }

    //추가 또는 편집될 가능성이 있는 새로운 사진을 담는 함수
    fun considerNewAlbumListUri(uriList: List<Uri>) {
        //편집화면에 보여주기 위해 담는 리스트
        _newUriListLiveData.value = uriList
    }

    //등록 또는 변경될 가능성이 있는 새로운 사진의 날짜를 담는 함수
    fun considerNewAlbumListDate(dateTime: String) {
        newUriList = newUriList.copy(
            photoDate = dateTime
        )
    }

    //등록 또는 변경될 가능성이 있는 사진의 제목을 담는 함수
    fun considerNewUriListTitle(editText: String) {
        newUriList = newUriList.copy(
            titleText = editText
        )
    }

    //사진을 추가하거나 편집하기 위한 준비를 마쳤는지 확인하는 함수
    fun checkPreparedNewAlbumList(): Boolean {
        val photoList = mutableListOf<String>()

        // photoList 초기화
        _newUriListLiveData.value?.let {
            it.forEach { photoList.add(it.toString()) }
        }

        newUriList = newUriList.copy(imageUris = photoList)

        return when (layoutModeLiveData.value) {
            "ADD" -> newUriList.imageUris.isNotEmpty() && newUriList.titleText.isNotEmpty() && newUriList.photoDate.isNotEmpty()
            "EDIT" -> {
                if (_newUriListLiveData.value?.isEmpty() == true) return true
                newUriList.titleText.isNotEmpty() && newUriList.photoDate.isNotEmpty()
            }
            else -> false
        }
    }






    //현재 사진을 새 사진으로 교체하는 함수
    fun updateAlbumList(index: Int) {
        albumList = _albumListLiveData.value?.toMutableList() ?: mutableListOf()

        when (_layoutModeLiveData.value) {
            "ADD" -> {
                _currentAlbumLiveData.value =
                    newUriList
                albumList.add(0, _currentAlbumLiveData.value!!)
                saveAlbumList()
            }

            "EDIT" -> {
               _currentAlbumLiveData.value = newUriList
                saveAlbumList()
                albumList[index] = _currentAlbumLiveData.value!!
            }
        }
        _albumListLiveData.value = albumList
    }

    //추가 또는 편집 중, 사진을 삭제하는 함수
    fun removeNewUriList(position: Int) {
        val list = _newUriListLiveData.value?.toMutableList()
        list?.removeAt(position)
        list?.let { considerNewAlbumListUri(it.toList()) }
    }

    //추가 또는 편집 중, 크롭할 사진의 인덱스를 담는 함수
    fun updateCropPosition(position: Int) {
        cropPosition = position
    }

    fun changeCropUri(uri: Uri) {
        val uriList = _newUriListLiveData.value!!.toMutableList()
        uriList!![cropPosition] = uri
        _newUriListLiveData.value = uriList
    }


    //삭제 관련 함수
    //삭제모드를 변경해주는 함수
    fun changeRemoveMode() {
        _removeModeLiveData.value =
            if (_removeModeLiveData.value == "COMPLETE") "REMOVE"
            else "COMPLETE"
    }

    //삭제모드를 취소해주는 함수
    fun cancelRemoveMode() {
        removeAlbumList.clear()
        removeAlbumList = _albumListLiveData.value!!.toMutableList()
        changeRemoveMode()
    }

    //삭제하려는 사진을 임시 저장 변수에 담아주는 함수
    fun considerRemoveAlbumList(photoList: GalleryModel, position: Int) {
        if (removeAlbumList[position].checked) {
            removeAlbumList[position] = photoList.copy(checked = false)
            _checkedAlbumLiveData.value = position
        } else {
            removeAlbumList[position] = photoList.copy(checked = true)
            _checkedAlbumLiveData.value = position
        }
    }

    //삭제가 반영된 리스트로 교체하는 함수
    fun updateRemovedAlbumList(mode: String) {
        when (mode) {
            "REMOVE" -> {
                removeAlbumList = _albumListLiveData.value!!.toMutableList()
            }

            "COMPLETE" -> {
                var list = removeAlbumList.filter { it.checked }


                viewModelScope.launch {
                    for (i in list) {
                        albumRepository.deleteGallery(i.uid)
                    }
                }
                removeAlbumList.removeIf { it.checked }

                _albumListLiveData.value = removeAlbumList
            }
        }
    }

    //날짜 내림차순으로 정렬해주는 함수
    private fun sortAlbumList() {
        val formatter = DateTimeFormatter.ofPattern("yyyy.M.d")
        val sortedAlbumList = _albumListLiveData.value
            ?.sortedByDescending { album -> LocalDate.parse(album.photoDate, formatter) }
        _albumListLiveData.value = sortedAlbumList!!
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