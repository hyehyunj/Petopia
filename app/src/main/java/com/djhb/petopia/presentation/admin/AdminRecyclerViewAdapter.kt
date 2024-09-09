package com.djhb.petopia.presentation.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.RecyclerviewGalleryHolderBinding

class AdminRecyclerViewAdapter(
    private var reportList: List<GalleryModel>,
    private val itemClickListener: (item: GalleryModel, position: Int) -> Unit,
    private val itemLongClickListener: (item: GalleryModel, position: Int) -> Unit,
) : RecyclerView.Adapter<AdminRecyclerViewAdapter.Holder>() {

    private var removeMode = "COMPLETE"

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
        holder.bind(reportList[position], position, removeMode)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    class Holder(
        private val binding: RecyclerviewGalleryHolderBinding,
        private val itemClickListener: (item: GalleryModel, position: Int) -> Unit,
        private val itemLongClickListener: (item: GalleryModel, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryModel, position: Int, removeMode: String) {
            binding.apply {
//                Log.i("GalleryRecyclerViewAdapter", "uri = ${item.imageUris[0].toUri()}")
//                galleryHolderIvTitle.setImageURI(item.imageUris[0].toUri())

                Glide.with(galleryHolderTvTitle.context)
                    .load(item.imageUris[0].toUri())
                    .centerCrop()
                    .into(galleryHolderIvTitle)
                galleryHolderTvTitle.text = item.titleText
//                binding.galleryHolderIvChecked.isVisible = false
                when (removeMode) {
                    "REMOVE" -> binding.galleryHolderIvUnchecked.isVisible = true
                    "COMPLETE" -> { binding.galleryHolderIvUnchecked.isVisible = false
                        binding.galleryHolderIvChecked.isVisible = false }
                }
                galleryHolder.setOnClickListener {
                    when (removeMode) {
                        "REMOVE" -> binding.galleryHolderIvChecked.isVisible = !item.checked
                        "COMPLETE" -> binding.galleryHolderIvChecked.isVisible = false
                    }
                    itemClickListener(item, position)
                }
                galleryHolder.setOnLongClickListener {
                    itemLongClickListener(item, position)
                    true
                }
            }
        }
    }

    fun updateList(galleryList: List<GalleryModel>) {
        reportList = galleryList
        notifyDataSetChanged()
    }

    fun updateCheckedList(galleryList: List<GalleryModel>, position: Int) {
        reportList = galleryList
        notifyItemChanged(position)
    }

    //모드로 전환해 전체 홀더에 선택박스를 표시해주거나 제거해주는 함수
    fun updateRemoveMode(pressedRemoveButton: String) {
        removeMode = pressedRemoveButton
        notifyDataSetChanged()
    }
}

