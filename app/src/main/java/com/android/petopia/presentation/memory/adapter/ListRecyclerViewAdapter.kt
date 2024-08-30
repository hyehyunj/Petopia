package com.android.petopia.presentation.memory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.petopia.data.Memory
import com.android.petopia.databinding.RecyclerviewMemoryListBinding

class ListRecyclerViewAdapter(
    private val itemClickListener: (item: Memory) -> Unit,
    private val itemLongClickListener: (item: Memory) -> Unit
) : ListAdapter<Memory, ListRecyclerViewAdapter.MemoryViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Memory>() {
            override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
                return oldItem.title == newItem.title
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
        private val itemClickListener: (item: Memory) -> Unit,
        private val itemLongClickListener: (item: Memory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Memory) {
            binding.tvMemoryListTitle.text = item.title
            binding.tvMemoryListContent.text = item.content
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