package com.android.petopia.presentation.letter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.R
import com.android.petopia.databinding.RecyclerviewWritepadListBinding

class LetterWritepadAdapter(
    private val backgroungList: List<Int>,
    private val onbackgroungClick: (Int) -> Unit
) : RecyclerView.Adapter<LetterWritepadAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_writepad_image)

        fun bind(item: Int) {
            imageView.setImageResource(item)
            itemView.setOnClickListener {
                onbackgroungClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LetterWritepadAdapter.ViewHolder {
        val binding = RecyclerviewWritepadListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: LetterWritepadAdapter.ViewHolder, position: Int) {
        val item = backgroungList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return backgroungList.size
    }
}