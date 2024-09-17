package com.djhb.petopia.presentation.community.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.PostHolderBinding

class PostAdapter(private val onClick: (post: PostModel) -> Unit): ListAdapter<PostModel, PostAdapter.PostItem>(object: DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("PostAdapter", "areItemsTheSame = ${oldItem.key == newItem.key}")
//        Log.i("PostAdapter", "oldItem.imageUris = ${oldItem.imageUris}")
//        Log.i("PostAdapter", "areItemsTheSame = ${oldItem.key == newItem.key}")

        return oldItem.key == newItem.key && oldItem.imageUris.hashCode() == newItem.imageUris.hashCode()
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("PostAdapter", "areContentTheSame = ${oldItem == newItem}")
//        Log.i("PostAdapter", "----------------------")
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItem {
//        Log.i("PostAdapter", "itemCount = ${itemCount}")
        val binding = PostHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItem(binding)
    }

    override fun onBindViewHolder(holder: PostItem, position: Int) {
        val item = getItem(position)

//        Log.i("PostAdapter", "item = ${item}")


        holder.title.text = item.title
        holder.viewCount.text = item.viewCount.toString()
//        holder.likeCount.text = item.likeCount.toString()
        holder.likeCount.text = item.likes.size.toString()
        holder.userId.text = item.writer.nickname
        holder.createdDate.text = DateFormatUtils.convertToPostFormat(item.createdDate)
        holder.binding.root.setOnClickListener {
            onClick(item)
        }

//        Log.i("PostAdapter", "uri = ${item.imageUris}")

        if(item.imageUris.size == 0)
            holder.mainImage.visibility = ImageView.INVISIBLE
        else{
            Glide
                .with(holder.mainImage.context)
                .load(item.imageUris[0].toUri())
                .centerCrop()
                .into(holder.mainImage)
        }
    }

    class PostItem(val binding: PostHolderBinding): RecyclerView.ViewHolder(binding.root){
        val mainImage = binding.ivMainImage
        var title = binding.tvTitle
        var viewCount = binding.tvViewCount
        var likeCount = binding.tvLikeCount
        var userId = binding.tvWriter
        var createdDate = binding.tvCreatedDate
    }
}