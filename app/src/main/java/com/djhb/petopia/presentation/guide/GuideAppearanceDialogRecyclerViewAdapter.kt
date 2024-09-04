package com.djhb.petopia.presentation.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.databinding.RecyclerviewGuideAppearanceDialogHolderBinding

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




