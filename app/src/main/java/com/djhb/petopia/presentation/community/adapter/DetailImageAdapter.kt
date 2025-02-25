package com.djhb.petopia.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.PagerPostDetailImageBinding

class DetailImageAdapter: ListAdapter<String, DetailImageAdapter.DetailImageHolder>(object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImageHolder {
        val binding = PagerPostDetailImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailImageHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailImageHolder, position: Int) {

        val item = getItem(position)

        Glide.with(holder.image.context)
            .load(item.toUri())
            .centerCrop()
            .into(holder.image)


    }

    class DetailImageHolder(val binding: PagerPostDetailImageBinding): ViewHolder(binding.root) {
        val image = binding.ivPostDetail
    }
}