package com.djhb.petopia.presentation.guide

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.R
import com.djhb.petopia.R.color.black
import com.djhb.petopia.data.PetAppearanceModel
import com.djhb.petopia.databinding.RecyclerviewGuideAppearanceDialogHolderBinding

class GuideAppearanceDialogRecyclerViewAdapter(
    private var appearanceList: List<PetAppearanceModel>,
    private val itemClickListener: (item: PetAppearanceModel, position: Int) -> Unit,
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
        if(position == currentSelectedIndex)  holder.bind(appearanceList[currentSelectedIndex], position)
        else holder.bind(appearanceList[position], position)
    }

    override fun getItemCount(): Int {
        return appearanceList.size
    }

    class Holder(
        private val binding: RecyclerviewGuideAppearanceDialogHolderBinding,
        private val itemClickListener: (item: PetAppearanceModel, position: Int) -> Unit,
        private val lastSelectedIndex: Int,
        private val currentSelectedIndex: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PetAppearanceModel, position: Int) {
            if(item.selected) {binding.guideAppearanceDialogHolder.setBackgroundResource(R.drawable.bg_guide_appearance_blue)
            binding.guideAppearanceDialogTv.setTextColor(Color.WHITE)}
            else {binding.guideAppearanceDialogHolder.setBackgroundResource(R.drawable.bg_guide_appearance_white)
                binding.guideAppearanceDialogTv.resources.getColor(R.color.main_blue)}


            binding.apply {
                guideAppearanceDialogTv.text = item.name
                guideAppearanceDialogHolder.setOnClickListener {
                      itemClickListener(item, position) }
            }
        }
    }

}




