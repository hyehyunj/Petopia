package com.android.petopia.presentation.letter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.LetterModel
import com.android.petopia.databinding.RecyclerviewLetterListBinding
import java.text.SimpleDateFormat
import java.util.Locale

class LetterListRecyclerViewAdapter(
    private val itemClickListener: (item: LetterModel) -> Unit,
    private val itemLongClickListener: (item: LetterModel) -> Unit
) : ListAdapter<LetterModel, LetterListRecyclerViewAdapter.LetterListViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LetterModel>() {
            override fun areItemsTheSame(oldItem: LetterModel, newItem: LetterModel): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: LetterModel, newItem: LetterModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LetterListRecyclerViewAdapter.LetterListViewHolder {
        val binding = RecyclerviewLetterListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LetterListViewHolder(binding, itemClickListener, itemLongClickListener)
    }

    override fun onBindViewHolder(
        holder: LetterListRecyclerViewAdapter.LetterListViewHolder,
        position: Int
    ) {
        val reversePosition = itemCount - position
        val item = getItem(position)
        holder.bind(item,reversePosition)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class LetterListViewHolder(
        private val binding: RecyclerviewLetterListBinding,
        private val itemClickListener: (item: LetterModel) -> Unit,
        private val itemLongClickListener: (item: LetterModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LetterModel, reversePosition: Int) {
            binding.tvLetterListTitle.text = item.title
            binding.tvLetterListContent.text = item.content
            binding.tvLetterListNumber.setText("#${reversePosition}")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date = dateFormat.format(item.createdDate)
            binding.tvLetterListDate.text = date

            binding.letterListViewHolder.setOnClickListener {
                itemClickListener(item)
            }
            binding.letterListViewHolder.setOnLongClickListener {
                itemLongClickListener(item)
                true
            }

        }
    }
}