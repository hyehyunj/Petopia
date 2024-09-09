package com.djhb.petopia.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.data.CommentModel
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.PostCommentHolderBinding


interface OnClickComment{
    fun onClickEdit(comment: CommentModel)
    fun onClickDelete(key: String)
}

class DetailCommentAdapter(private val onClickComment: OnClickComment)
    : ListAdapter<CommentModel, DetailCommentAdapter.CommentHolder>(object: DiffUtil.ItemCallback<CommentModel>(){
    override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = PostCommentHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {

        val item = getItem(position)

        holder.content.text = item.content
        holder.writer.text = item.writer.nickname
        holder.createdDate.text = DateFormatUtils.convertToPostFormat(item.createdDate)

        if(LoginData.loginUser.id == item.writer.id) {
            holder.editButton.visibility = TextView.VISIBLE
            holder.deleteButton.visibility = TextView.VISIBLE

            holder.editButton.setOnClickListener {
                onClickComment.onClickEdit(item)
            }

            holder.deleteButton.setOnClickListener {
                onClickComment.onClickDelete(item.key)
            }

        } else{
            holder.editButton.visibility = TextView.GONE
            holder.deleteButton.visibility = TextView.GONE
        }

    }

    class CommentHolder(val binding: PostCommentHolderBinding): ViewHolder(binding.root){
        val content = binding.tvContent
        val writer = binding.tvWriter
        val createdDate = binding.tvCreatedDate
        val editButton = binding.tvEditComment
        val deleteButton = binding.tvDeleteComment
    }
}