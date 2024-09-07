package com.djhb.petopia.presentation.community.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.djhb.petopia.DateFormatUtils
import com.djhb.petopia.R
import com.djhb.petopia.data.PostModel
import com.djhb.petopia.databinding.PostRankHolderBinding

class RankPostAdapter(private val onClick: (post: PostModel) -> Unit): ListAdapter<PostModel,RankPostAdapter.RankPostHolder>(object : DiffUtil.ItemCallback<PostModel>(){
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        return oldItem == newItem
    }
}) {

    private val rankMedalImages = mutableListOf(
        R.drawable.icon_1st_rank_temp,
        R.drawable.icon_2nd_rank_temp,
        R.drawable.icon_3rd_rank_temp
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankPostHolder {
        val binding = PostRankHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankPostHolder(binding)
    }

    override fun onBindViewHolder(holder: RankPostHolder, position: Int) {

        val item = getItem(position)
        Log.i("RankPostAdapter", "item = ${item}")

        holder.title.text = item.title
        holder.viewCount.text = item.viewCount.toString()
        holder.likeCount.text = item.likeCount.toString()
        holder.userId.text = item.writer.id
        holder.createdDate.text = DateFormatUtils.convertToPostFormat(item.createdDate)
        holder.rankImage.setImageResource(rankMedalImages[position])

        holder.binding.root.setOnClickListener {
            onClick(item)
        }

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

    class RankPostHolder(val binding: PostRankHolderBinding): ViewHolder(binding.root) {
        val rankImage = binding.ivRank
        val mainImage = binding.ivMainImage
        var title = binding.tvTitle
        var viewCount = binding.tvViewCount
        var likeCount = binding.tvLikeCount
        var userId = binding.tvWriter
        var createdDate = binding.tvCreatedDate
    }

}