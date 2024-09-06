package com.djhb.petopia.presentation.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.R
import com.djhb.petopia.databinding.RecyclerviewGuideAppearanceDialogHolderBinding

class GuideAppearanceDialogRecyclerViewAdapter(
    private var item: List<String>,
    private val itemClickListener: (item: String, position: Int) -> Unit,
) : RecyclerView.Adapter<GuideAppearanceDialogRecyclerViewAdapter.Holder>() {

    private var lastSelectedIndex = 0
    private var currentSelectedIndex = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewGuideAppearanceDialogHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return Holder(binding, itemClickListener, lastSelectedIndex, currentSelectedIndex)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(item[position], position)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    class Holder(
        private val binding: RecyclerviewGuideAppearanceDialogHolderBinding,
        private val itemClickListener: (item: String, position: Int) -> Unit,
        private val lastSelectedIndex: Int,
        private val currentSelectedIndex: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) {

            binding.apply {
                guideAppearanceDialogTv.text = item
                guideAppearanceDialogHolder.setOnClickListener {
                      itemClickListener(item, position) }
            }
        }
    }


    fun updateSelectedIndex(index: Int) {
        lastSelectedIndex = currentSelectedIndex
        currentSelectedIndex = index

        notifyItemChanged(lastSelectedIndex)
        notifyItemChanged(currentSelectedIndex)
    }
}




