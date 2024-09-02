package com.android.petopia.presentation.community.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.petopia.DateFormatUtils
import com.android.petopia.R
import com.android.petopia.data.PostModel
import com.android.petopia.databinding.PostRankHolderBinding

class RankPostAdapter(): ListAdapter<PostModel,RankPostAdapter.RankPostHolder>(object : DiffUtil.ItemCallback<PostModel>(){
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

        holder.title = item.title
        holder.viewCount = item.viewCount.toString()
        holder.likeCount = item.likeCount.toString()
        holder.userId = item.writer.id
        holder.createdDate = DateFormatUtils.convertToPostFormat(item.createdDate)
        holder.rankImage.setImageResource(rankMedalImages[position])

        if(item.imageUris.size == 0)
            holder.mainImage.setImageResource(R.drawable.icon_no_image_temp)
        else
            holder.mainImage.setImageURI(item.imageUris[0].toUri())


    }

    class RankPostHolder(val binding: PostRankHolderBinding): ViewHolder(binding.root) {
        val rankImage = binding.ivRank
        val mainImage = binding.ivMainImage
        var title = binding.tvTitle.text
        var viewCount = binding.tvViewCount.text
        var likeCount = binding.tvLikeCount.text
        var userId = binding.tvWriter.text
        var createdDate = binding.tvCreatedDate.text

    }

}