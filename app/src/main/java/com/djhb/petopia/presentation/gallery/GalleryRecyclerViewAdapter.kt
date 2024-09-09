package com.djhb.petopia.presentation.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.RecyclerviewGalleryHolderBinding

class GalleryRecyclerViewAdapter(
    private var item: List<GalleryModel>,
    private val itemClickListener: (item: GalleryModel, position: Int) -> Unit,
    private val itemLongClickListener: (item: GalleryModel, position: Int) -> Unit,
) : RecyclerView.Adapter<GalleryRecyclerViewAdapter.Holder>() {

    companion object {
        private const val TAG = "GalleryFragment"
    }

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
        holder.bind(item[position], position, removeMode)
    }

    override fun getItemCount(): Int {
        return item.size
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
        item = galleryList
        notifyDataSetChanged()
    }

    fun updateCheckedList(galleryList: List<GalleryModel>, position: Int) {
        item = galleryList
        notifyItemChanged(position)
    }

    //모드로 전환해 전체 홀더에 선택박스를 표시해주거나 제거해주는 함수
    fun updateRemoveMode(pressedRemoveButton: String) {
        removeMode = pressedRemoveButton
        notifyDataSetChanged()
    }
}


//    private val itemClickListener: (item: GalleryModel) -> Unit,
//    private val itemLongClickListener: (item: GalleryModel) -> Unit
//) : ListAdapter<GalleryModel, GalleryRecyclerViewAdapter.Holder>(diffUtil) {
//private var removeMode = false
//    companion object {
//        val diffUtil = object : DiffUtil.ItemCallback<GalleryModel>() {
//            override fun areItemsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
//                return oldItem.uId == newItem.uId
//            }
//
//            override fun areContentsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val binding =
//            RecyclerviewGalleryHolderBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return Holder(binding, itemClickListener, itemLongClickListener, removeMode)
//    }
//
//
//    override fun onBindViewHolder(holder: Holder, position: Int) {
//            holder.bind(getItem(position))
//    }
//
//    class Holder(
//        private val binding: RecyclerviewGalleryHolderBinding,
//        private val itemClickListener: (GalleryModel) -> Unit,
//        private val itemLongClickListener: (item: GalleryModel) -> Unit,
//        private val removeMode: (Boolean)
//    ) :
//        RecyclerView.ViewHolder(binding.root) {
//
//
//        fun bind(item: GalleryModel) {
//            binding.apply {
//                galleryHolderIvTitle.setImageURI(item.titleImage.toUri())
//                galleryHolderTvTitle.text = item.titleText
//                galleryHolder.setOnClickListener {
//                    itemClickListener(item)
//                }
//                galleryHolder.setOnLongClickListener {
//                    itemLongClickListener(item)
//                    true
//                }
//            }
//            if(removeMode) binding.galleryHolderCb.isVisible = true
//        }
//    }
//
//
//    fun appearCheckBox(removeMode: Boolean) {
//        this.removeMode = removeMode
//    }





