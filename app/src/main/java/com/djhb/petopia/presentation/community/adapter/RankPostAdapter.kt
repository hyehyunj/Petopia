package com.djhb.petopia.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.Table
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.GalleryPostMainHolderBinding
import com.djhb.petopia.databinding.PostRankHolderBinding

class RankPostAdapter(val postType: String, private val onClick: (post: PostModel) -> Unit): ListAdapter<PostModel,ViewHolder>(object : DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("RankPostAdapter", "oldItem.imageUris = ${oldItem.imageUris.hashCode()}")
//        Log.i("RankPostAdapter", "newItem.imageUris = ${newItem.imageUris.hashCode()}")
        return oldItem.key == newItem.key
                && oldItem.imageUris.size == newItem.imageUris.size
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
//        Log.i("RankPostAdapter", "oldItem == newItem : ${oldItem == newItem}")
//        return oldItem == newItem
        return false
    }
}) {

//    private val rankMedalImages = mutableListOf(
//        R.drawable.icon_1st_rank_temp,
//        R.drawable.icon_2nd_rank_temp,
//        R.drawable.icon_3rd_rank_temp
//    )
    private val rankMedalImages = mutableListOf(
        R.drawable.icon_community_1,
        R.drawable.icon_community_2,
        R.drawable.icon_community_3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        Log.i("RankPostAdapter", "onCreateViewHolder")
//        val binding = PostRankHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RankPostHolder(binding)

        return when(viewType) {
            TYPE_GALLERY -> GalleryRankPostHolder(GalleryPostMainHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> RankPostHolder(PostRankHolderBinding.inflate(LayoutInflater.from(parent.context), parent,false))
        }

//        return when(viewType) {
//            TYPE_GALLERY -> GalleryRankPostHolder(GalleryPostMainHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            else -> RankPostHolder(PostRankHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(postType) {
            Table.GALLERY_POST.tableName -> TYPE_GALLERY
            else -> TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        Log.i("RankPostAdapter", "onBindViewHolder : ${item}")
//        Log.i("RankPostAdapter", "item = ${item}")
        if(postType == Table.GALLERY_POST.tableName) {

            val currentHolder = holder as GalleryRankPostHolder

            currentHolder.rankImage.visibility = ImageView.VISIBLE
            currentHolder.rankImage.setImageResource(rankMedalImages[position])

            currentHolder.binding.root.setOnClickListener {
                onClick(item)
            }

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



        } else {

            val currentHolder = holder as RankPostHolder
//            currentHolder.rankImage.visibility = ImageView.INVISIBLE

            currentHolder.title.text = item.title
            currentHolder.viewCount.text = item.viewCount.toString()
//        currentHolder.likeCount.text = item.likeCount.toString()
//            currentHolder.likeCount.text = item.likes.size.toString()
            currentHolder.likeCount.text = item.likeCount.toString()
            currentHolder.commentCount.text = item.commentCount.toString()
            currentHolder.userId.text = item.writer.nickname
            currentHolder.createdDate.text = DateFormatUtils.convertToPostFormat(item.createdDate)
            currentHolder.rankImage.setImageResource(rankMedalImages[position])

            currentHolder.binding.root.setOnClickListener {
                onClick(item)
            }

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

    class RankPostHolder(val binding: PostRankHolderBinding): ViewHolder(binding.root) {
        val rankImage = binding.ivRank
        val mainImage = binding.ivMainImage
        val title = binding.tvTitle
        val viewCount = binding.tvViewCount
        val likeCount = binding.tvLikeCount
        val commentCount = binding.tvCommentCount
        val userId = binding.tvWriter
        val createdDate = binding.tvCreatedDate


    }

    class GalleryRankPostHolder(val binding: GalleryPostMainHolderBinding): ViewHolder(binding.root) {
        val rankImage = binding.ivRankGalleryPost
        val mainImage = binding.ivGalleryPost
    }

    companion object{
        private const val TYPE_NORMAL = 0
        private const val TYPE_GALLERY = 1
    }

}