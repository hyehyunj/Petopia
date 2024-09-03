package com.android.petopia.presentation.guide

import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.GalleryModel
import com.android.petopia.databinding.RecyclerviewGalleryHolderBinding
import com.android.petopia.databinding.RecyclerviewGuideAppearanceDialogHolderBinding

class GuideAppearanceDialogRecyclerViewAdapter(
    private var item: List<String>,
    private val itemClickListener: (item: String) -> Unit,
) : RecyclerView.Adapter<GuideAppearanceDialogRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewGuideAppearanceDialogHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return Holder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }

    class Holder(
        private val binding: RecyclerviewGuideAppearanceDialogHolderBinding,
        private val itemClickListener: (item: String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {

            binding.apply {
                guideAppearanceDialogTv.text = item
                guideAppearanceDialogHolder.setOnClickListener { itemClickListener(item) }
            }
        }
    }
}




