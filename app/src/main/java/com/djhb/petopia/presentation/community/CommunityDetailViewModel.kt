package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.Table
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.LikeModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.data.remote.CommentRepository
import com.djhb.petopia.data.remote.CommentRepositoryImpl
import com.djhb.petopia.data.remote.LikeRepository
import com.djhb.petopia.data.remote.LikeRepositoryImpl
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel(private val postType: Table): ViewModel() {


    private val postTypeToTables = mutableMapOf(
        Table.QUESTION_POST to listOf(Table.QUESTION_POST, Table.QUESTION_COMMENT, Table.QUESTION_LIKE),
        Table.INFORMATION_POST to listOf(Table.INFORMATION_POST, Table.INFORMATION_COMMENT, Table.INFORMATION_LIKE),
        Table.GALLERY_POST to listOf(Table.GALLERY_POST, Table.GALLERY_COMMENT, Table.GALLERY_LIKE)
    )

    private val currentTables = postTypeToTables[postType]


    private val _currentPost = MutableLiveData<PostModel>()
    val currentPost get() = _currentPost

    private val _imageUris = MutableLiveData<List<String>>()
    val imageUris get() = _imageUris

    var imageUriResults = mutableListOf<String>()

    private val _comments = MutableLiveData<MutableList<CommentModel>>()
    val comments get() = _comments

    var commentsResult = mutableListOf<CommentModel>()

    var currentUserLike = LikeModel()

    private val _likes = MutableLiveData<MutableList<LikeModel>>()
    val likes get()= _likes

    private var likesResult = mutableListOf<LikeModel>()

    private val postRepository: PostRepository by lazy {
        PostRepositoryImpl(currentTables?.get(0)?:Table.NONE)
    }

    private val commentRepository: CommentRepository by lazy {
        CommentRepositoryImpl(currentTables?.get(1)?:Table.NONE)
    }

    private val likeRepository: LikeRepository by lazy {
        LikeRepositoryImpl(currentTables?.get(2)?:Table.NONE)
    }

    fun selectPostFromKey(postKey: String) {

        viewModelScope.launch {
            currentPost.value = postRepository.selectPostFromKey(postKey)
        }

    }

    suspend fun selectDetailImageUris(key: String) {
        viewModelScope.launch {
//            imageUriResults = async { postRepository.selectDetailImages(key)}.await()
//            Log.i("CommunityDetailViewModel", "imageUriResults.size = ${imageUriResults.size}")
//            _imageUris.value = imageUriResults
//            _imageUris.value = ima
            imageUriResults.clear()
            val imageUris = postRepository.selectDetailImagesTest(key)

            for (imageUri in imageUris) {
                imageUriResults.add(postRepository.selectDownloadUri(imageUri))
            }

            _imageUris.value = imageUriResults
        }
    }

    suspend fun createComment(comment: CommentModel){
//        commentsResult.add(0, commentRepository.createComment(comment))
        commentsResult.add(commentRepository.createComment(comment))
        _comments.value = commentsResult
    }

    suspend fun selectAllCommentFromPost(postKey: String) {
        commentsResult.clear()
        val posts = commentRepository.selectAllCommentsFromPost(postKey)

        posts.removeIf {
            LoginData.loginUser.reportList.contains(it.writer.id)
        }

        commentsResult.addAll(posts)
        _comments.value = commentsResult
    }

    suspend fun updateComment(comment: CommentModel) {
        commentRepository.updateComment(comment)
        for ((commentIndex, commentModel) in commentsResult.withIndex()) {
            if(commentModel.key == comment.key) {
                commentsResult[commentIndex] = comment
                break
            }
        }
        _comments.value = commentsResult
    }

    suspend fun deleteComment(commentKey: String) {
        commentRepository.deleteComment(commentKey)

        commentsResult.removeIf { comment ->
            comment.key == commentKey
        }
        _comments.value = commentsResult
    }

    fun createLike(like: LikeModel) {
        viewModelScope.launch {
            likeRepository.createLike(like)
            likesResult.add(like)
            likes.value = likesResult
        }
    }

    fun deleteLike(likeKey: String) {

        viewModelScope.launch {

            async { likeRepository.deleteLike(likeKey)}.await()

            likesResult.removeIf {
                it.key == likeKey
            }

            _likes.value = likesResult

//            _likes.value?.removeIf{
//                it.key == likeKey
//            }

        }

    }

    suspend fun selectLikeFromUser(postKey: String, userId: String){
        viewModelScope.launch {
            currentUserLike = async {likeRepository.selectLikeFromUser(postKey, userId)}.await()
        }
    }

    suspend fun selectAllLikeFromPost(postKey: String) {
        viewModelScope.launch {
            likesResult = likeRepository.selectLikeList(postKey)
            _likes.value = likesResult
        }
    }

}