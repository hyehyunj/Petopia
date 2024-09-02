package com.android.petopia.presentation.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.RecyclerviewGalleryHolderBinding

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
        Log.d(TAG, "온바인드${position}")
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
                galleryHolderIvTitle.setImageURI(item.imageUris[0].toUri())
                galleryHolderTvTitle.text = item.titleText

                galleryHolder.setOnClickListener {
                    Log.d("어댑터", "${item.checked}")
                    when (removeMode) {
                        "REMOVE" -> binding.galleryHolderCb.isVisible = true
                        "COMPLETE" -> binding.galleryHolderCb.isVisible = false
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





