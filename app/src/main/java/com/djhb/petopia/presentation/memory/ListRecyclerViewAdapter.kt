package com.djhb.petopia.presentation.memory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.data.Memory
import com.djhb.petopia.databinding.RecyclerviewMemoryListBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListRecyclerViewAdapter(
    private val itemClickListener: (item: Memory) -> Unit,
    private val itemLongClickListener: (item: Memory) -> Unit
) : ListAdapter<Memory, ListRecyclerViewAdapter.MemoryViewHolder>(diffUtil) {

    //다중선택에서 선택된 아이템을 담아두는 리스트
    val selcetedItems = mutableListOf<Memory>()

    //삭제모드 여부
    var isDeleteMode = false
    var isCleared = false

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Memory>() {
            override fun areItemsTheSame(oldItem: Memory, newItem: Memory): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: Memory, newItem: Memory): Boolean {
                return oldItem == newItem
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
        val reversePosition = itemCount - position
        val item = getItem(position)
        holder.bind(item, reversePosition)
    }

    inner class MemoryViewHolder(
        private val binding: RecyclerviewMemoryListBinding,
        private val itemClickListener: (item: Memory) -> Unit,
        private val itemLongClickListener: (item: Memory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Memory, reversePosition: Int) {
            binding.tvMemoryListTitle.text = item.title
            binding.tvMemoryListContent.text = item.content
            binding.tvMemoryListNumber.setText("#${reversePosition}")


            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val date = dateFormat.format(item.createdDate)
            binding.tvMemoryListDate.text = date

            if (isCleared) {
                binding.ivMemoryDeleteCheck.visibility = View.GONE
            }

            binding.memoryListViewHolder.setOnClickListener {
                //삭제모드일때 클릭과 그냥 클릭 구분
                if (isDeleteMode) {
                    toggleSelection(item)
                    binding.ivMemoryDeleteCheck.visibility =
                        if (selcetedItems.contains(item)) View.VISIBLE else View.GONE
                } else {
                    itemClickListener(item)
                }

            }
        }
    }

    //삭제모드 토글
    fun toggleDeleteMode() {
        isDeleteMode = !isDeleteMode
        isCleared = false
        selcetedItems.clear()
        notifyDataSetChanged()
    }

    fun toggleSelection(item: Memory) {
        if (selcetedItems.contains(item)) {
            selcetedItems.remove(item)
        } else {
            selcetedItems.add(item)
        }
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Memory> {
        return selcetedItems.toList()
    }

    fun clearSelections() {
        isCleared = true
        selcetedItems.clear() // 선택된 항목 초기화
        notifyDataSetChanged() // 어댑터에 변경 알림
    }
}