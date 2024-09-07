package com.djhb.petopia.presentation.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.remote.CommentRepositoryImpl
import com.djhb.petopia.data.remote.PostRepository
import com.djhb.petopia.data.remote.PostRepositoryImpl
import kotlinx.coroutines.launch

class CommunityDetailViewModel: ViewModel() {

    private val _imageUris = MutableLiveData<MutableList<String>>()
    val imageUris get() = _imageUris

    var imageUriResults = listOf<String>()

    private val _comments = MutableLiveData<MutableList<CommentModel>>()
    val comments get() = _comments

    var commentsResult = mutableListOf<CommentModel>()

    private val postRepository = PostRepositoryImpl()
    private val commentRepository = CommentRepositoryImpl()


    suspend fun selectDetailImageUris(key: String) {
        viewModelScope.launch {
            imageUriResults = postRepository.selectDetailImages(key)
//            _imageUris.value = ima
        }
    }

    suspend fun createComment(comment: CommentModel){
        commentsResult.add(0, commentRepository.createComment(comment))
        _comments.value = commentsResult
    }

    suspend fun selectAllCommentFromPost(postKey: String) {
        commentsResult.clear()
        commentsResult.addAll(commentRepository.selectAllCommentsFromPost(postKey))
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

}