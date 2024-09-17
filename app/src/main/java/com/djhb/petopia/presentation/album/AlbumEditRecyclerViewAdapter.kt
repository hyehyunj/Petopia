package com.djhb.petopia.presentation.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.CreatePostImageHolderBinding
import com.djhb.petopia.databinding.RecyclerviewAlbumEditHolderBinding
import com.djhb.petopia.databinding.RecyclerviewAlbumHolderBinding

class AlbumEditRecyclerViewAdapter(
    private var item: List<Uri>,
    private val itemClickListener: (item: Uri, position: Int) -> Unit,
    private val itemLongClickListener: (item: Uri, position: Int) -> Unit,
) : RecyclerView.Adapter<AlbumEditRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewAlbumEditHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding, itemClickListener, itemLongClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(item[position], position)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    class Holder(
        private val binding: RecyclerviewAlbumEditHolderBinding,
        private val itemClickListener: (item: Uri, position: Int) -> Unit,
        private val itemLongClickListener: (item: Uri, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Uri, position: Int) {

            binding.apply {
                Glide.with(ivAddImage.context)
                    .load(item)
                    .centerCrop()
                    .into(ivAddImage)

                    ivDeleteImage.setOnClickListener {
                    itemClickListener(item, position)
                }
                ivDeleteImage.setOnLongClickListener {
                    itemLongClickListener(item, position)
                    true
                }
            }
        }
    }

    fun updateList(galleryList: List<Uri>) {
        item = galleryList
        notifyDataSetChanged()
    }

    fun updateCheckedList(galleryList: List<Uri>, position: Int) {
        item = galleryList
        notifyItemChanged(position)
    }

}





