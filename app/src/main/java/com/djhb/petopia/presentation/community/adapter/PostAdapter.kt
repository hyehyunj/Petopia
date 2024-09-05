package com.djhb.petopia.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.PostHolderBinding

class PostAdapter(): ListAdapter<PostModel, PostAdapter.PostItem>(object: DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItem {
        val binding = PostHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItem(binding)
    }

    override fun onBindViewHolder(holder: PostItem, position: Int) {
        val item = getItem(position)

        holder.title = item.title
        holder.viewCount = item.viewCount.toString()
        holder.likeCount = item.likeCount.toString()
        holder.userId = item.writer.id
        holder.createdDate = DateFormatUtils.convertToPostFormat(item.createdDate)

        if(item.imageUris.size == 0)
            holder.mainImage.setImageResource(R.drawable.icon_no_image_temp)
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
        var title = binding.tvTitle.text
        var viewCount = binding.tvViewCount.text
        var likeCount = binding.tvLikeCount.text
        var userId = binding.tvWriter.text
        var createdDate = binding.tvCreatedDate.text
    }
}