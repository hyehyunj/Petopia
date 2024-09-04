package com.djhb.petopia.presentation.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djhb.petopia.data.PostModel

class CommunityViewModel : ViewModel() {

    private val _rankPosts = MutableLiveData<PostModel>()
    val rankPosts get() = _rankPosts

    val rankPostResult = mutableListOf<PostModel>()




}