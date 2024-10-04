package com.djhb.petopia.presentation.gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.RecyclerviewAlbumEditHolderBinding

//앨범 편집 프래그먼트에서 사진 목록을 보여주는 리사이클러뷰 어댑터
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
        private val removeItemClickListener: (item: Uri, position: Int) -> Unit,
        private val cropItemClickListener: (item: Uri, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Uri, position: Int) {

            binding.apply {
                Glide.with(albumEditHolderIv.context)
                    .load(item)
                    .centerCrop()
                    .into(albumEditHolderIv)
                    //삭제버튼 클릭이벤트
                    albumEditHolderBtnRemove.setOnClickListener {
                    removeItemClickListener(item, position)
                }
                //크롭버튼 클릭이벤트
                albumEditHolderBtnCrop.setOnClickListener {
                    cropItemClickListener(item, position)
                }
            }
        }
    }
    //사진리스트를 업데이트해주는 함수
    fun updateList(galleryList: List<Uri>) {
        item = galleryList
        notifyDataSetChanged()
    }
}





