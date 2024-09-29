package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.FilteringType
import com.djhb.petopia.Table
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.remote.CommentRepository
import com.djhb.petopia.data.remote.CommentRepositoryImpl
import com.djhb.petopia.data.remote.LikeRepository
import com.djhb.petopia.data.remote.LikeRepositoryImpl
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class CommunityViewModel(val postType: Table = Table.NONE) : ViewModel() {

    private val postTypeToTables = mutableMapOf(
        Table.QUESTION_POST to listOf(Table.QUESTION_POST, Table.QUESTION_COMMENT, Table.QUESTION_LIKE),
        Table.INFORMATION_POST to listOf(Table.INFORMATION_POST, Table.INFORMATION_COMMENT, Table.INFORMATION_LIKE),
        Table.GALLERY_POST to listOf(Table.GALLERY_POST, Table.GALLERY_COMMENT, Table.GALLERY_LIKE)
    )

    private val currentTables = postTypeToTables[postType]

    private val _rankPosts = MutableLiveData<MutableList<PostModel>>()
    val rankPosts get() = _rankPosts

    private val _rankPostsWithMainImage = MutableLiveData<MutableList<PostModel>>()
    val rankPostsWithMainImage get() = _rankPostsWithMainImage

    var rankPostResult = mutableListOf<PostModel>()
    var isCompleteRankPost = MutableLiveData<Boolean>()

    private val _searchPosts = MutableLiveData<MutableList<PostModel>>()
    val searchPost get() = _searchPosts

    var searchPostResult = Collections.synchronizedList(mutableListOf<PostModel>())

    private val _searchPostWithImages = MutableLiveData<MutableList<PostModel>>()
    val searchPostWithImages get() = _searchPostWithImages

    private val searchPostWithImagesResult = mutableListOf<PostModel>()

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

    var nextPostIndex = 0


    private val postRepository: PostRepository by lazy {
        PostRepositoryImpl(currentTables?.get(0)?:Table.NONE)
    }

    private val commentRepository: CommentRepository by lazy {
        CommentRepositoryImpl(currentTables?.get(1)?:Table.NONE)
    }

    private val likeRepository: LikeRepository by lazy {
        LikeRepositoryImpl(currentTables?.get(2)?:Table.NONE)
    }


    private lateinit var lastSnapshot: DocumentSnapshot

//    var isProgressing = false
    private val _isProgressing = MutableLiveData<Boolean>()
    val isProgressing get() = _isProgressing

    suspend fun createPost(post: PostModel, imageUris: MutableList<String>){

        viewModelScope.launch {

            async { postRepository.createPost(post, imageUris)}.await()
            postRepository.createPostImages(post, imageUris)

        }

    }

    fun createPostImages(post: PostModel, imageUris: MutableList<String>) {
        viewModelScope.launch {
            async { postRepository.createPostImages(post, imageUris)}.await()
        }
    }


    suspend fun selectRankList(categories: List<FilteringType>) {

        viewModelScope.launch {
            rankPostResult.clear()
            if(categories.size > 0)
                rankPostResult = postRepository.selectRankPostsWhereFiltering(categories)
            else
                rankPostResult = postRepository.selectRankPosts()

            rankPostResult.removeIf{
                LoginData.loginUser.reportList.contains(it.writer.id)
            }

            for (postModel in rankPostResult) {
                postModel.likes.addAll(likeRepository.selectLikeList(postModel.key))
            }

            for(postIndex in 0..rankPostResult.size-1) {
                val post = rankPostResult[postIndex]
                val commentCount = commentRepository.selectCommentCount(post.key)
                rankPostResult[postIndex] = rankPostResult[postIndex].copy(commentCount = commentCount)
            }

//            val imageUris = mutableListOf<StorageReference?>()
//
//            for (post in rankPostResult) {
//                post.imageUris.clear()
//                imageUris.add(postRepository.selectPostMainImage(post))
//            }
//
//            for ((uriIndex, uri) in imageUris.withIndex()) {
//                if(uri != null) {
//                    rankPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
//                }
//            }

            _rankPosts.value = rankPostResult

        }

    }

    suspend fun selectRankListWithImage(){
        viewModelScope.launch {
            val imageUris = mutableListOf<StorageReference?>()

            for (post in rankPostResult) {
                post.imageUris.clear()
                imageUris.add(postRepository.selectPostMainImage(post))
            }

            for ((uriIndex, uri) in imageUris.withIndex()) {
                if(uri != null) {
                    rankPostResult[uriIndex].imageUris.add(postRepository.selectDownloadUri(uri))
                }
            }
            rankPostsWithMainImage.value = rankPostResult
        }
    }

    suspend fun selectInitPostList(categories: List<FilteringType>){
        viewModelScope.launch {
            searchPostResult.clear()
            addedSearchResult.clear()
            _isProgressing.value = true

            var documents =
                if(postType == Table.GALLERY_POST) {
                    when {
                        categories.size > 0 -> postRepository.selectInitPostsWhereFiltering(categories, limit = 18)
                        else -> postRepository.selectInitPosts(limit = 18)
                    }
                } else {
                    when {
                        categories.size > 0 -> postRepository.selectInitPostsWhereFiltering(categories)
                        else -> postRepository.selectInitPosts()
                    }
                }


            if (documents.size > 0) {
                lastSnapshot = documents[documents.size - 1]

                addedSearchResult.addAll(postRepository.convertToPostModel(documents))

                addedSearchResult.removeIf {
                    LoginData.loginUser.reportList.contains(it.writer.id)
                }

                while (documents.size > 0
                    && ((postType == Table.GALLERY_POST && addedSearchResult.size < 18)
                            || (postType != Table.GALLERY_POST && addedSearchResult.size < 10))){

                    documents =
                        if(postType == Table.GALLERY_POST) {
                            when {
                                categories.size > 0 -> postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories, limit = 18)
                                else -> postRepository.selectInitPosts(limit = 18)
                            }
                        } else {
                            when {
                                categories.size > 0 -> postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                                else -> postRepository.selectInitPosts()
                            }
                        }

//                        when {
//                        categories.size > 0 ->
//                            postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
//                        else ->
//                            postRepository.selectNextPosts(lastSnapshot)
//                    }
                    if (documents.size > 0) {
                        lastSnapshot = documents[documents.size - 1]

                        addedSearchResult.addAll(postRepository.convertToPostModel(documents))
                        addedSearchResult.removeIf {
                            LoginData.loginUser.reportList.contains(it.writer.id)
                        }
                    }
                }

                for (postModel in addedSearchResult) {
                    postModel.likes.addAll(likeRepository.selectLikeList(postModel.key))
                }

                for(postIndex in 0..addedSearchResult.size-1) {
                    val post = addedSearchResult[postIndex]
                    val commentCount = commentRepository.selectCommentCount(post.key)
                    addedSearchResult[postIndex] = addedSearchResult[postIndex].copy(commentCount = commentCount)
                }

                searchPostResult.addAll(addedSearchResult)
                _addedSearchPost.value = addedSearchResult

            } else
                isProgressing.value = false
        }
    }

    suspend fun selectNextPostList(categories: List<FilteringType>) {
//        Log.i("CommunityViewModel", "start1 selectNextPostList()")
        viewModelScope.launch {
            addedSearchResult.clear()
            _isProgressing.value = true
            var documents = when {
                categories.size > 0 ->
                    postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                else ->
                    postRepository.selectNextPosts(lastSnapshot)
            }

            if(documents.size > 0) {
                lastSnapshot = documents[documents.size - 1]
//                addedSearchResult = postRepository.convertToPostModel(documents)
                addedSearchResult.addAll(postRepository.convertToPostModel(documents))

                addedSearchResult.removeIf {
                    LoginData.loginUser.reportList.contains(it.writer.id)
                }

                while (documents.size > 0
                    && ((postType == Table.GALLERY_POST && addedSearchResult.size < 9)
                            || (postType != Table.GALLERY_POST && addedSearchResult.size < 5))){

                    documents =
                        if(postType == Table.GALLERY_POST) {
                            when {
                                categories.size > 0 -> postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories, limit = 9)
                                else -> postRepository.selectInitPosts(limit = 9)
                            }
                        } else {
                            when {
                                categories.size > 0 -> postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
                                else -> postRepository.selectInitPosts()
                            }
                        }

//                    when {
//                        categories.size > 0 ->
//                            postRepository.selectNextPostsWhereFiltering(lastSnapshot, categories)
//                        else ->
//                            postRepository.selectNextPosts(lastSnapshot)
//                    }
//                    Log.i("CommunityViewModel", "need more post documents.size = ${documents.size}")
                    if (documents.size > 0) {
                        lastSnapshot = documents[documents.size - 1]

                        addedSearchResult.addAll(postRepository.convertToPostModel(documents))
                        addedSearchResult.removeIf {
                            LoginData.loginUser.reportList.contains(it.writer.id)
                        }
                    }
                }

                for(postIndex in 0..addedSearchResult.size-1) {
                    val post = addedSearchResult[postIndex]
                    val commentCount = commentRepository.selectCommentCount(post.key)
                    Log.i("CommunityViewModel", "post title = ${post.title}")
                    Log.i("CommunityViewModel", "commentCount = ${commentCount}")
                    addedSearchResult[postIndex] = addedSearchResult[postIndex].copy(commentCount = commentCount)
                }

                searchPostResult.addAll(addedSearchResult)
//                _searchPostWithImages.value = searchPostResult
//                _addedSearchPost.value = addedSearchResult
                _addedSearchPost.value = searchPostResult

            } else
                isProgressing.value = false
        }
    }

    suspend fun selectAllImageList(){
        viewModelScope.launch {
            val references = mutableListOf<StorageReference?>()


//            for(searchIndex in 0..addedSearchResult.size-1) {
            for(searchIndex in 0..addedSearchResult.size-1) {
                val post = addedSearchResult[searchIndex]
                post.imageUris.clear()
                references.add(postRepository.selectPostMainImage(post))
            }

            val imageUris = mutableListOf<String>()

            for (uri in references) {
                if(uri != null) {
                    imageUris.add(postRepository.selectDownloadUri(uri))
                } else
                    imageUris.add("")
            }
            _postImageUris.value = imageUris
        }
    }

    fun combinePostToImages(imageUris: MutableList<String>){

        for ((uriIndex, imageUri) in imageUris.withIndex()) {
            Log.i("CommunityViewModel", "searchPostResult's index = ${nextPostIndex + uriIndex}")
            if(imageUri != "") {
                Log.i("CommunityViewModel", "exist image")

//                addedSearchResult[uriIndex].imageUris.add(imageUri)
                searchPostResult[nextPostIndex + uriIndex].imageUris.add(imageUri)
            }
        }

//        searchPostResult.addAll(addedSearchResult)

        for (postModel in searchPostResult) {
            Log.i("CommunityViewModel", "searchPostResult = ${postModel}")
        }

        _searchPostWithImages.value = searchPostResult



        isProgressing.value = false

//        nextPostIndex += searchPostResult.size
        nextPostIndex += addedSearchResult.size
        Log.i("CommunityViewModel", "nextPostIndex = ${nextPostIndex}")
    }

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