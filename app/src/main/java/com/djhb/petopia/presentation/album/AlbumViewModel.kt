package com.djhb.petopia.presentation.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djhb.petopia.data.GalleryModel

//앨범 뷰모델
class AlbumViewModel() :
    ViewModel() {

    //앨범 페이지
    private val _albumPageLiveData = MutableLiveData<Int>()
    val albumPageLiveData: LiveData<Int> = _albumPageLiveData

    //데이터베이스에서 사용자의 앨범 리스트를 불러오는 함수
    fun updateCurrentPage(page: Int) {
        _albumPageLiveData.value = page
    }
}
