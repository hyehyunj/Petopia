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
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.Table
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.GalleryPostMainHolderBinding
import com.djhb.petopia.databinding.PostHolderBinding

class PostAdapter(val postType: String, private val onClick: (post: PostModel) -> Unit): ListAdapter<PostModel, ViewHolder>(object: DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("PostAdapter", "areItemsTheSame = ${oldItem.key == newItem.key}")
//        Log.i("PostAdapter", "oldItem.imageUris = ${oldItem.imageUris}")
//        Log.i("PostAdapter", "areItemsTheSame = ${oldItem.key == newItem.key}")

        return oldItem.key == newItem.key && oldItem.imageUris.hashCode() == newItem.imageUris.hashCode()
//        return oldItem.key == newItem.key
//        return false
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("PostAdapter", "areContentTheSame = ${oldItem == newItem}")
//        Log.i("PostAdapter", "----------------------")
//        return oldItem == newItem
        return false
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        Log.i("PostAdapter", "itemCount = ${itemCount}")
//        return PostItem(binding)

        return when(viewType) {
            TYPE_GALLERY -> GalleryPostItem(GalleryPostMainHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> PostItem(PostHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(postType) {
            Table.GALLERY_POST.tableName -> TYPE_GALLERY
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

//        Log.i("PostAdapter", "item = ${item}")
        if(postType == Table.GALLERY_POST.tableName) {
//            Log.i("PostAdapter : ", "itemCount = ${itemCount}")
            val currentHolder = holder as GalleryPostItem

            currentHolder.binding.root.setOnClickListener {
                onClick(item)
            }

            if (item.imageUris.size == 0)
                currentHolder.mainImage.visibility = ImageView.INVISIBLE
            else {
                currentHolder.mainImage.visibility = ImageView.VISIBLE
                Glide.with(currentHolder.mainImage.context)
                    .load(item.imageUris[0].toUri())
                    .centerCrop()
                    .into(currentHolder.mainImage)
            }
        } else {
            val currentHolder = holder as PostItem
            currentHolder.title.text = item.title
            currentHolder.viewCount.text = item.viewCount.toString()
//        hocurrentHder.likeCount.text = item.likeCount.toString()
//            currentHolder.likeCount.text = item.likes.size.toString()
            currentHolder.likeCount.text = item.likeCount.toString()
            currentHolder.commentCount.text = item.commentCount.toString()
            currentHolder.userId.text = item.writer.nickname
            currentHolder.createdDate.text = DateFormatUtils.convertToPostFormat(item.createdDate)
            currentHolder.binding.root.setOnClickListener {
                onClick(item)
            }

//        Log.i("PostAdapter", "uri = ${item.imageUris}")

            if (item.imageUris.size == 0)
                currentHolder.mainImage.visibility = ImageView.INVISIBLE
            else {
                currentHolder.mainImage.visibility = ImageView.VISIBLE

                Glide
                    .with(currentHolder.mainImage.context)
                    .load(item.imageUris[0].toUri())
                    .centerCrop()
                    .into(currentHolder.mainImage)
            }
        }
    }

    class PostItem(val binding: PostHolderBinding): ViewHolder(binding.root){
        val mainImage = binding.ivMainImage
        val title = binding.tvTitle
        val viewCount = binding.tvViewCount
        val likeCount = binding.tvLikeCount
        val commentCount = binding.tvCommentCount
        val userId = binding.tvWriter
        val createdDate = binding.tvCreatedDate
    }

    class GalleryPostItem(val binding: GalleryPostMainHolderBinding): ViewHolder(binding.root) {
        val mainImage = binding.ivGalleryPost
    }

    companion object{
        private const val TYPE_NORMAL = 0
        private const val TYPE_GALLERY = 1
    }
}