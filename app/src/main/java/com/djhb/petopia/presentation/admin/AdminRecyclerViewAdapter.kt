package com.djhb.petopia.presentation.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.databinding.RecyclerviewAdminHolderBinding
import com.google.firebase.database.core.Repo

class AdminRecyclerViewAdapter(
//    private var reportList: List<ReportModel>,
    private val itemClickListener: (item: ReportModel, position: Int) -> Unit,
    private val itemLongClickListener: (item: ReportModel, position: Int) -> Unit,
) : ListAdapter<ReportModel, AdminRecyclerViewAdapter.Holder>(object : DiffUtil.ItemCallback<ReportModel>(){
    override fun areItemsTheSame(oldItem: ReportModel, newItem: ReportModel): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: ReportModel, newItem: ReportModel): Boolean {
        return oldItem == newItem
    }
}) {


    //    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val binding =
//            RecyclerviewAdminHolderBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return Holder(binding, itemClickListener, itemLongClickListener)
//    }
//
//    override fun onBindViewHolder(holder: Holder, position: Int) {
//        holder.bind(reportList[position], position)
//    }
//
//    override fun getItemCount(): Int {
//        return reportList.size
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecyclerviewAdminHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        holder.binding.root.setOnClickListener {
            itemClickListener(item, position)
        }

        holder.binding.root.setOnLongClickListener {
            itemLongClickListener(item, position)
            true
        }
    }

    class Holder(
        val binding: RecyclerviewAdminHolderBinding,
//        private val itemClickListener: (item: ReportModel, position: Int) -> Unit,
//        private val itemLongClickListener: (item: ReportModel, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReportModel) {
            binding.apply {

                adminTvTitle.text = item.reasonType.toString()
                adminTvReporter.text = item.reporterId

//                adminHolder.setOnClickListener {
//
//                    itemClickListener(item, position)
//                }
//                adminHolder.setOnLongClickListener {
//                    itemLongClickListener(item, position)
//                    true
//                }
            }
        }
    }


}

