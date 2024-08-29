package com.android.petopia.presentation.memory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.MemoryModel
import com.android.petopia.databinding.RecyclerviewMemoryListBinding

class ListRecyclerViewAdapter(
    private val itemClickListener: (item: MemoryModel) -> Unit,
    private val itemLongClickListener: (item: MemoryModel) -> Unit
) : ListAdapter<MemoryModel, ListRecyclerViewAdapter.MemoryViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemoryModel>() {
            override fun areItemsTheSame(oldItem: MemoryModel, newItem: MemoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MemoryModel, newItem: MemoryModel): Boolean {
                return oldItem.memoryTitle == newItem.memoryTitle
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val binding = RecyclerviewMemoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemoryViewHolder(binding, itemClickListener, itemLongClickListener)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MemoryViewHolder(
        private val binding: RecyclerviewMemoryListBinding,
        private val itemClickListener: (item: MemoryModel) -> Unit,
        private val itemLongClickListener: (item: MemoryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemoryModel) {
            binding.tvMemoryListTitle.text = item.memoryTitle
            binding.tvMemoryListContent.text = item.memoryContent
            binding.memoryListViewHolder.setOnClickListener {
                itemClickListener(item)
            }
            binding.memoryListViewHolder.setOnLongClickListener {
                itemLongClickListener(item)
                true
            }

        }

    }
}