package com.djhb.petopia.presentation.community.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.djhb.petopia.R
import com.djhb.petopia.databinding.CreatePostImageHolderBinding


interface OnclickImage{
    fun onClickAddImage()
    fun onClickDeleteImage(uri: String)
}

class CreateImageAdapter(private val onClickImage: OnclickImage): ListAdapter<String, CreateImageAdapter.CreateImageHolder>(object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateImageHolder {
        Log.i("createImageAdapter", "start : createViewHolder() ")
        val binding = CreatePostImageHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreateImageHolder(binding)

    }

    override fun onBindViewHolder(holder: CreateImageHolder, position: Int) {
        val item = getItem(position)
        if(position == 0) {
            holder.btnDelete.visibility = ImageView.GONE
            holder.image.setImageResource(R.drawable.img_community_add_photo)
            holder.binding.root.setOnClickListener {
                onClickImage.onClickAddImage()
            }
        }
        else {
            holder.btnDelete.visibility = ImageView.VISIBLE
            holder.btnDelete.setOnClickListener {
                onClickImage.onClickDeleteImage(item)
            }
            Glide.with(holder.image.context)
                .load(item.toUri())
                .centerCrop()
                .into(holder.image)
        }

    }

    class CreateImageHolder(val binding: CreatePostImageHolderBinding): ViewHolder(binding.root){
        val image = binding.ivAddImage
        val btnDelete = binding.ivDeleteImage
    }
}