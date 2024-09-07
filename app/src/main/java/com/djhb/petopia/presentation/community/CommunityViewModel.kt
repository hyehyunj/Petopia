package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.remote.PostRepositoryImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _rankPosts = MutableLiveData<MutableList<PostModel>>()
    val rankPosts get() = _rankPosts

    var rankPostResult = mutableListOf<PostModel>()
    var isCompleteRankPost = MutableLiveData<Boolean>()

    private val _searchPosts = MutableLiveData<MutableList<PostModel>>()
    val searchPost get() = _searchPosts
    var searchPostResult = mutableListOf<PostModel>()

    private val _postImageUris = MutableLiveData<MutableList<String>>()
    val postImageUris get() = _postImageUris

    private val postRepository = PostRepositoryImpl()

    fun createRankList() {

        viewModelScope.launch {
            Log.i("CommunityViewModel", "createRankList 1")
            val selectRankPosts = postRepository.selectRankPosts()
            Log.i("CommunityViewModel", "createRankList 2")
            Log.i("CommunityViewModel", "selectRankPosts = ${selectRankPosts}")
            for (post in selectRankPosts) {
                rankPostResult.add(postRepository.selectPostMainImage(post))
            }
//            val includedImages = postRepository.selectPostMainImage(selectRankPosts)
//            Log.i("CommunityViewModel", "createRankList 3 : ${includedImages}")
            _rankPosts.value = rankPostResult

//            val selectRankPosts = async { postRepository.selectRankPosts() }
//            val result = selectRankPosts.await()
//            for ((index, model) in result.withIndex()) {
//                val includedImages = async { postRepository.selectPostMainImage(model) }
//                rankPostResult.add(includedImages.await())
//                if(index == result.size-1)
//                    isCompleteRankPost.value = true
//            }


        }
//        viewModelScope.launch {
//            val rankPosts = async { postRepository.selectRankPosts()
//            }
//            rankPostResult = rankPosts.await()
//            Log.i("CommunityViewModel", "rankPostResult.size = ${rankPostResult.size}")
//        }
    }

    fun createAllList(){
        viewModelScope.launch {
            val selectRankPosts = postRepository.selectPosts()
            Log.i("CommunityViewModel", "createRankList 2")
            Log.i("CommunityViewModel", "selectRankPosts.size = ${selectRankPosts.size}")
            for (post in selectRankPosts) {
                searchPostResult.add(postRepository.selectPostMainImage(post))
            }

//            val includedImages = postRepository.selectPostMainImage(selectRankPosts)
//            Log.i("CommunityViewModel", "createRankList 3 : ${includedImages}")
            _searchPosts.value = searchPostResult
        }
    }

    fun listRankPost(){
        _rankPosts.value = rankPostResult
    }

//    suspend fun createRankListImages(rankPosts: MutableList<PostModel>){
////        viewModelScope.launch {
////            val list = async { postRepository.selectPostMainImage(rankPosts) }
////            _rankPosts.value = list.await()
////            Log.i("CommunityViewModel", "rankPost.size = ${_rankPosts.value?.size}")
////        }
//    }

    fun updatePost(post: PostModel) {
        viewModelScope.launch {
            postRepository.updatePost(post)
        }
    }

}