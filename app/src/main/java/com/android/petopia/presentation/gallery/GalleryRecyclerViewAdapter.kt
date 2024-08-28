package com.android.petopia.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.RecyclerviewGalleryHolderBinding

class GalleryRecyclerViewAdapter(
    private val itemClickListener: (item: GalleryModel) -> Unit,
    private val itemLongClickListener: (item: GalleryModel) -> Unit
) : ListAdapter<GalleryModel, GalleryRecyclerViewAdapter.Holder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GalleryModel>() {
            override fun areItemsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem.uId == newItem.uId
            }

            override fun areContentsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewGalleryHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding, itemClickListener, itemLongClickListener)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(getItem(position))
    }

    class Holder(
        private val binding: RecyclerviewGalleryHolderBinding,
        private val itemClickListener: (GalleryModel) -> Unit,
        private val itemLongClickListener: (item: GalleryModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: GalleryModel) {
            binding.apply {
                galleryHolderIvTitle.setImageURI(item.titleImage.toUri())
                galleryHolderTvTitle.text = item.titleText
                galleryHolder.setOnClickListener {
                    itemClickListener(item)
                }
                galleryHolder.setOnLongClickListener {
                    itemLongClickListener(item)
                    true
                }
            }
        }
    }
}




