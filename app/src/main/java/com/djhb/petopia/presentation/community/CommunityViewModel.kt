package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.remote.PostRepositoryImpl
import com.google.firebase.storage.StorageReference
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

    fun createPost(post: PostModel, imageUris: MutableList<String>){

        viewModelScope.launch {
            postRepository.createPost(post, imageUris)
            selectRankList()
            selectAllList()
        }

    }

    fun createPostImages(post: PostModel, imageUris: MutableList<String>) {
        viewModelScope.launch {
            postRepository.createPostImages(post, imageUris)
        }
    }


    fun selectRankList() {

        viewModelScope.launch {
            rankPostResult.clear()
            rankPostResult = postRepository.selectRankPosts()
//            _rankPosts.value = rankPostResult

            val imageUris = mutableListOf<StorageReference?>()

            for (post in rankPostResult) {
                post.imageUris.clear()
                imageUris.add(postRepository.selectPostMainImage(post))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
                Log.i("CommunityViewModel", "rank uri = ${uri}")
                if(uri != null) {
                    rankPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
                }
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

    fun selectAllList(){
        viewModelScope.launch {
            searchPostResult.clear()
//            val allPosts = postRepository.selectPosts()
            searchPostResult = postRepository.selectPosts()

            val imageUris = mutableListOf<StorageReference?>()

            for (post in searchPostResult) {
                post.imageUris.clear()
                imageUris.add(postRepository.selectPostMainImage(post))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
                Log.i("CommunityViewModel", "rank uri = ${uri}")
                if(uri != null) {
                    searchPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
                }
            }
//            Log.i("CommunityViewModel", "createRankList 2")
//            Log.i("CommunityViewModel", "selectRankPosts.size = ${allPosts.size}")
//            for (post in allPosts) {
//                searchPostResult.add(postRepository.selectPostMainImage(post))
//            }

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

            for ((postIndex,postModel) in rankPostResult.withIndex()) {
                if(postModel.key == post.key) {
                    rankPostResult[postIndex] = post
                    break
                }
            }

            _rankPosts.value = rankPostResult

            for ((postIndex,postModel) in searchPostResult.withIndex()) {
                if(postModel.key == post.key) {
                    searchPostResult[postIndex] = post
                    break
                }
            }
            _searchPosts.value = searchPostResult
        }
    }

    fun addPostViewCount(postKey: String) {
        viewModelScope.launch {
            postRepository.addPostViewCount(postKey)
        }
    }

    fun deletePost(postKey: String) {
        viewModelScope.launch {
            postRepository.deletePost(postKey)

            rankPostResult.removeIf{ post ->
                post.key == postKey
            }

            _rankPosts.value = rankPostResult

            searchPostResult.removeIf { post ->
                post.key == postKey
            }
            _searchPosts.value = searchPostResult
        }
    }

    fun deletePostImages(postKey: String){
        viewModelScope.launch {
            postRepository.deletePostImages(postKey)
        }
    }


}