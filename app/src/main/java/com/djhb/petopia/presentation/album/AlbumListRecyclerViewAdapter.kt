package com.djhb.petopia.presentation.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.data.GalleryModel
import com.djhb.petopia.databinding.RecyclerviewAlbumHolderBinding

class AlbumListRecyclerViewAdapter(
    private var item: List<GalleryModel>,
    private val itemClickListener: (item: GalleryModel, position: Int) -> Unit,
    private val itemLongClickListener: (item: GalleryModel, position: Int) -> Unit,
) : RecyclerView.Adapter<AlbumListRecyclerViewAdapter.Holder>() {

    private var removeMode = "COMPLETE"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewAlbumHolderBinding.inflate(
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
        private val binding: RecyclerviewAlbumHolderBinding,
        private val itemClickListener: (item: GalleryModel, position: Int) -> Unit,
        private val itemLongClickListener: (item: GalleryModel, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryModel, position: Int, removeMode: String) {

            binding.apply {
//                Log.i("GalleryRecyclerViewAdapter", "uri = ${item.imageUris[0].toUri()}")
//                galleryHolderIvTitle.setImageURI(item.imageUris[0].toUri())

                if(item.imageUris.size > 0) {
                Glide.with(albumHolderTvTitle.context)
                    .load(item.imageUris[0].toUri())
                    .centerCrop()
                    .into(albumHolderIvTitle) }
                albumHolderTvTitle.text = item.titleText
//                binding.albumHolderIvChecked.isVisible = false
                when (removeMode) {
                    "REMOVE" -> binding.albumHolderIvUnchecked.isVisible = true
                    "COMPLETE" -> { binding.albumHolderIvUnchecked.isVisible = false
                        binding.albumHolderIvChecked.isVisible = false }
                }
                albumHolder.setOnClickListener {
                    when (removeMode) {
                        "REMOVE" -> binding.albumHolderIvChecked.isVisible = !item.checked
                        "COMPLETE" -> binding.albumHolderIvChecked.isVisible = false
                    }
                    itemClickListener(item, position)
                }
                albumHolder.setOnLongClickListener {
                    itemLongClickListener(item, position)
                    true
                }
            }
        }
    }

    //앨범 리스트를 갱신해주는 함수
    fun updateList(albumList: List<GalleryModel>) {
        item = albumList
        notifyDataSetChanged()
    }

    //선택여부를 확인해 반영해주는 함수
    fun updateCheckedList(albumList: List<GalleryModel>, position: Int) {
        item = albumList
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
//                albumHolderIvTitle.setImageURI(item.titleImage.toUri())
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





