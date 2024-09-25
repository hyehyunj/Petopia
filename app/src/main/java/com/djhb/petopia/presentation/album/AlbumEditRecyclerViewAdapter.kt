package com.djhb.petopia.presentation.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.RecyclerviewAlbumEditHolderBinding

class AlbumEditRecyclerViewAdapter(
    private var item: List<Uri>,
    private val removeItemClickListener: (item: Uri, position: Int) -> Unit,
    private val cropItemClickListener: (item: Uri, position: Int) -> Unit,
) : RecyclerView.Adapter<AlbumEditRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewAlbumEditHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding, removeItemClickListener, cropItemClickListener)
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
        private val cropItemClickListener: (item: Uri, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Uri, position: Int) {

            binding.apply {
                Glide.with(albumEditHolderIv.context)
                    .load(item)
                    .centerCrop()
                    .into(albumEditHolderIv)

                    albumEditHolderBtnRemove.setOnClickListener {
                    itemClickListener(item, position)
                }
                albumEditHolderBtnCrop.setOnClickListener {
                    cropItemClickListener(item, position)
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





