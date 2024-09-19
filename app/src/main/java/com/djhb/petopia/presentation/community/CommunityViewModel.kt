package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.FilteringType
import com.djhb.petopia.data.LikeModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.remote.LikeRepository
import com.djhb.petopia.data.remote.LikeRepositoryImpl
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class CommunityViewModel : ViewModel() {

    private val _rankPosts = MutableLiveData<MutableList<PostModel>>()
    val rankPosts get() = _rankPosts

    var rankPostResult = mutableListOf<PostModel>()
    var isCompleteRankPost = MutableLiveData<Boolean>()

    private val _searchPosts = MutableLiveData<MutableList<PostModel>>()
    val searchPost get() = _searchPosts

    var searchPostResult = Collections.synchronizedList(mutableListOf<PostModel>())

    private val _searchPostWithImages = MutableLiveData<MutableList<PostModel>>()
    val searchPostWithImages get() = _searchPostWithImages

    private val _postImageUris = MutableLiveData<MutableList<String>>()
    val postImageUris get() = _postImageUris

    private val _postAddImageUris = MutableLiveData<MutableList<String>>()
    val postAddImageUris get() = _postAddImageUris

    private val _addedSearchPost = MutableLiveData<MutableList<PostModel>>()
    val addedSearchPost get() = _addedSearchPost

    private var addedSearchResult = Collections.synchronizedList(mutableListOf<PostModel>())

    private val _filteringCategories = MutableLiveData<MutableList<FilteringType>>()
    val filteringCategories get() = _filteringCategories

    val filteringResult = mutableListOf<FilteringType>()


    private val postRepository: PostRepository by lazy {
        PostRepositoryImpl()
    }

    private val likeRepository: LikeRepository by lazy {
        LikeRepositoryImpl()
    }

    private lateinit var lastSnapshot: DocumentSnapshot

//    var isProgressing = false
    private val _isProgressing = MutableLiveData<Boolean>()
    val isProgressing get() = _isProgressing

    suspend fun createPost(post: PostModel, imageUris: MutableList<String>){

        viewModelScope.launch {

//            Log.i("CommunityViewModel", "start createPost")
            async { postRepository.createPost(post, imageUris)}.await()
            postRepository.createPostImages(post, imageUris)
//            Log.i("CommunityViewModel", "before add create post = ${searchPostResult.size}")
//            searchPostResult.add(0, post)
//            Log.i("CommunityViewModel", "after add create post = ${searchPostResult.size}")
//            _searchPosts.value = searchPostResult
//            selectRankList()
//            selectAllList()
        }

    }

    fun createPostImages(post: PostModel, imageUris: MutableList<String>) {
        viewModelScope.launch {
            async { postRepository.createPostImages(post, imageUris)}.await()
        }
    }


    suspend fun selectRankList(categories: List<FilteringType>) {

//        Log.i("CommunityViewModel", "start1 selectRankList()")
        viewModelScope.launch {
//            Log.i("CommunityViewModel", "123. start selectRankList()")
            rankPostResult.clear()
            if(categories.size > 0)
                rankPostResult = postRepository.selectRankPostsWhereFiltering(categories)
            else
                rankPostResult = postRepository.selectRankPosts()
//            _rankPosts.value = rankPostResult

            rankPostResult.removeIf{
                LoginData.loginUser.reportList.contains(it.writer.id)
            }

            for (postModel in rankPostResult) {
                postModel.likes.addAll(likeRepository.selectLikeList(postModel.key))
            }

            val imageUris = mutableListOf<StorageReference?>()

            for (post in rankPostResult) {
                post.imageUris.clear()
                imageUris.add(postRepository.selectPostMainImage(post))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
//                Log.i("CommunityViewModel", "rank uri = ${uri}")
                if(uri != null) {
                    rankPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
                }
            }

//            val includedImages = postRepository.selectPostMainImage(selectRankPosts)
//            Log.i("CommunityViewModel", "createRankList 3 : ${includedImages}")
            _rankPosts.value = rankPostResult
//            Log.i("CommunityViewModel", "123. end selectRankList()")

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

    suspend fun selectInitPostList(categories: List<FilteringType>){
//        Log.i("CommunityViewModel", "start1 selectAllList()")
        viewModelScope.launch {
//            Log.i("CommunityViewModel", "123. start selectInitAllList()")
            searchPostResult.clear()
            addedSearchResult.clear()
//            val allPosts = postRepository.selectPosts()
//            searchPostResult = postRepository.selectPosts()
            _isProgressing.value = true

            var documents = when {
                categories.size > 0 -> postRepository.selectInitPostsWhereFiltering(categories)
                else -> postRepository.selectInitPosts()
            }

            if (documents.size > 0) {
                lastSnapshot = documents[documents.size - 1]

//            searchPostResult = postRepository.convertToPostModel(documents)


                addedSearchResult.addAll(postRepository.convertToPostModel(documents))

                addedSearchResult.removeIf {
                    LoginData.loginUser.reportList.contains(it.writer.id)
                }

                while (addedSearchResult.size < 8 && documents.size > 0){

//                    documents = postRepository.selectNextPosts(lastSnapshot)
                    documents = when {
                        categories.size > 0 ->
                            postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                        else ->
                            postRepository.selectNextPosts(lastSnapshot)
                    }
//                    Log.i("CommunityViewModel", "need more post documents.size = ${documents.size}")
                    if (documents.size > 0) {
                        lastSnapshot = documents[documents.size - 1]

//            searchPostResult = postRepository.convertToPostModel(documents)
                        addedSearchResult.addAll(postRepository.convertToPostModel(documents))
//                        Log.i("CommunityViewModel", "more addedSearchResult.size = ${addedSearchResult.size}")
                        addedSearchResult.removeIf {
                            LoginData.loginUser.reportList.contains(it.writer.id)
                        }
                    }
                }

                for (postModel in addedSearchResult) {
                    postModel.likes.addAll(likeRepository.selectLikeList(postModel.key))
                }


//                Log.i("CommunityViewModel", "addedSearchResult = ${addedSearchResult.size}")

//            val imageUris = mutableListOf<StorageReference?>()
//
//            for (post in searchPostResult) {
//                post.imageUris.clear()
//                imageUris.add(postRepository.selectPostMainImage(post))
//            }
//
//            for ((uriIndex, uri) in imageUris.withIndex()) {
//                Log.i("CommunityViewModel", "rank uri = ${uri}")
//                if(uri != null) {
//                    searchPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
//                }
//            }
//            Log.i("CommunityViewModel", "createRankList 2")
//            Log.i("CommunityViewModel", "selectRankPosts.size = ${allPosts.size}")
//            for (post in allPosts) {
//                searchPostResult.add(postRepository.selectPostMainImage(post))
//            }

//            val includedImages = postRepository.selectPostMainImage(selectRankPosts)
//            Log.i("CommunityViewModel", "createRankList 3 : ${includedImages}")
//            _searchPosts.value = searchPostResult

//                searchPostResult.addAll(addedSearchResult)

//            _searchPosts.value = addedSearchResult

                _addedSearchPost.value = addedSearchResult



//            selectAllImageList()

//                val references = mutableListOf<StorageReference?>()
//
////            for (post in searchPostResult) {
////                post.imageUris.clear()
////                references.add(postRepository.selectPostMainImage(post))
////            }
//
////            for(searchIndex in 0..searchPostResult.size-1) {
////                val post = searchPostResult[searchIndex]
////                post.imageUris.clear()
////                references.add(postRepository.selectPostMainImage(post))
////            }
//
//                for (searchIndex in 0..addedSearchResult.size - 1) {
//                    val post = addedSearchResult[searchIndex]
//                    Log.i("CommunityViewModel", "index =${searchIndex}, addSearchResult = ${post}")
//                    post.imageUris.clear()
//                    val imageReference = postRepository.selectPostMainImage(post)
//                    Log.i("CommunityViewModel", "reference = ${imageReference}")
//                    references.add(imageReference)
//                }
//
//                val imageUris = mutableListOf<String>()
//
//                for ((uriIndex, uri) in references.withIndex()) {
////                Log.i("CommunityViewModel", "rank uri = ${uri}")
//                    if (uri != null) {
////                    searchPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
//                        imageUris.add(postRepository.selectDownloadUri(uri))
//                    } else
//                        imageUris.add("")
//                }
//
//
//                _postImageUris.value = imageUris.toMutableList()
//
//            Log.i("CommunityViewModel", "123. end selectInitAllList()")
            } else
                isProgressing.value = false
        }
    }

    suspend fun selectNextPostList(categories: List<FilteringType>) {
//        Log.i("CommunityViewModel", "start1 selectNextPostList()")
        viewModelScope.launch {
            addedSearchResult.clear()
//            Log.i("CommunityViewModel", "123. start selectNextPostList()")
//            searchPostResult.clear()
//            val allPosts = postRepository.selectPosts()
//            searchPostResult = postRepository.selectPosts()
            _isProgressing.value = true
            var documents = when {
                categories.size > 0 ->
                    postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                else ->
                    postRepository.selectNextPosts(lastSnapshot)
            }

            if(documents.size > 0) {
                lastSnapshot = documents[documents.size - 1]
//                searchPostResult.addAll(postRepository.convertToPostModel(documents))
                addedSearchResult = postRepository.convertToPostModel(documents)
//                selectAllImageList()

                addedSearchResult.removeIf {
                    LoginData.loginUser.reportList.contains(it.writer.id)
                }

                while (addedSearchResult.size < 8 && documents.size > 0){
                    documents = when {
                        categories.size > 0 ->
                            postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                        else ->
                            postRepository.selectNextPosts(lastSnapshot)
                    }
//                    Log.i("CommunityViewModel", "need more post documents.size = ${documents.size}")
                    if (documents.size > 0) {
                        lastSnapshot = documents[documents.size - 1]

//            searchPostResult = postRepository.convertToPostModel(documents)
                        addedSearchResult.addAll(postRepository.convertToPostModel(documents))
//                        Log.i("CommunityViewModel", "more addedSearchResult.size = ${addedSearchResult.size}")
                        addedSearchResult.removeIf {
                            LoginData.loginUser.reportList.contains(it.writer.id)
                        }
                    }
                }

                _addedSearchPost.value = addedSearchResult

//                val references = mutableListOf<StorageReference?>()
//
////            for (post in searchPostResult) {
////                post.imageUris.clear()
////                references.add(postRepository.selectPostMainImage(post))
////            }
//
////            for(searchIndex in 0..searchPostResult.size-1) {
////                val post = searchPostResult[searchIndex]
////                post.imageUris.clear()
////                references.add(postRepository.selectPostMainImage(post))
////            }
//
//                for(searchIndex in 0..addedSearchResult.size-1) {
//                    val post = addedSearchResult[searchIndex]
//                    post.imageUris.clear()
//                    references.add(postRepository.selectPostMainImage(post))
//                }
//
//                val imageUris = mutableListOf<String>()
//
//                for ((uriIndex, uri) in references.withIndex()) {
////                    Log.i("CommunityViewModel", "rank uri = ${uri}")
//                    if(uri != null) {
////                    searchPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
//                        imageUris.add(async { postRepository.selectDownloadUri(uri)}.await())
//                    } else
//                        imageUris.add("")
//                }
//
//                Log.i("CommunityViewModel", "123. nestPostList addedResult.size = ${addedSearchResult.size}")
//
//                _postImageUris.value = imageUris
//                _searchPosts.value = searchPostResult
            } else
                isProgressing.value = false
        }
    }

    suspend fun selectAllImageList(){
        viewModelScope.launch {
//            Log.i("CommunityViewModel", "123. start selectAllImageList()")
            val references = mutableListOf<StorageReference?>()

//            for (post in searchPostResult) {
//                post.imageUris.clear()
//                references.add(postRepository.selectPostMainImage(post))
//            }

//            for(searchIndex in 0..searchPostResult.size-1) {
//                val post = searchPostResult[searchIndex]
//                post.imageUris.clear()
//                references.add(postRepository.selectPostMainImage(post))
//            }

            for(searchIndex in 0..addedSearchResult.size-1) {
                val post = addedSearchResult[searchIndex]
                post.imageUris.clear()
                references.add(postRepository.selectPostMainImage(post))
            }

            val imageUris = mutableListOf<String>()

            for ((uriIndex, uri) in references.withIndex()) {
//                Log.i("CommunityViewModel", "rank uri = ${uri}")
                if(uri != null) {
//                    searchPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
                    imageUris.add(postRepository.selectDownloadUri(uri))
                } else
                    imageUris.add("")
            }
            _postImageUris.value = imageUris
//            Log.i("CommunityViewModel", "123. end selectAllImageList()")
//            _searchPosts.value = searchPostResult
        }
    }

    fun combinePostToImages(imageUris: MutableList<String>){
//        Log.i("CommunityViewModel", "123. start combinePostToImages()")
//        Log.i("CommunityViewModel", "123. uri.size = ${imageUris.size}")
//        Log.i("CommunityViewModel", "123. addedSearchResult.size = ${addedSearchResult.size}")

        for ((uriIndex, imageUri) in imageUris.withIndex()) {
//            Log.i("CommunityViewModel", "before add imageUri = ${searchPostResult[uriIndex].imageUris}")
            if(imageUri != "")
                addedSearchResult[uriIndex].imageUris.add(imageUri)
//            Log.i("CommunityViewModel", "after add imageUri = ${searchPostResult[uriIndex].imageUris}")
        }
//        _searchPosts.value = searchPostResult
        searchPostResult.addAll(addedSearchResult)
        _searchPostWithImages.value = searchPostResult
        isProgressing.value = false
//        Log.i("CommunityViewModel", "123. end combinePostToImages()")
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

    suspend fun addPostViewCount(postKey: String) {
        viewModelScope.launch {
            postRepository.addPostViewCount(postKey)
        }
    }

    suspend fun deletePost(postKey: String) {
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

    suspend fun deletePostImages(postKey: String){
        viewModelScope.launch {
            postRepository.deletePostImages(postKey)
        }
    }

    suspend fun selectPostFromUser(userId: String): MutableList<PostModel> {
        return withContext(Dispatchers.IO) {
            async { postRepository.selectPostFromUser(userId)}.await()
        }

    }

    fun addCategories(category: FilteringType){
        filteringResult.add(category)
        _filteringCategories.value = filteringResult
    }

    fun deleteCategories(category: FilteringType) {
        filteringResult.removeIf {
            it == category
        }
        _filteringCategories.value = filteringResult
    }

}