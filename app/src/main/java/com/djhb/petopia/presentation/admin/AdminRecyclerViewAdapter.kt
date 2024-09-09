package com.djhb.petopia.presentation.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.djhb.petopia.data.ReportModel
import com.djhb.petopia.databinding.RecyclerviewAdminHolderBinding

class AdminRecyclerViewAdapter(
    private var reportList: List<ReportModel>,
    private val itemClickListener: (item: ReportModel, position: Int) -> Unit,
    private val itemLongClickListener: (item: ReportModel, position: Int) -> Unit,
) : RecyclerView.Adapter<AdminRecyclerViewAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RecyclerviewAdminHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return Holder(binding, itemClickListener, itemLongClickListener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(reportList[position], position)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    class Holder(
        private val binding: RecyclerviewAdminHolderBinding,
        private val itemClickListener: (item: ReportModel, position: Int) -> Unit,
        private val itemLongClickListener: (item: ReportModel, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReportModel, position: Int) {
            binding.apply {

                adminTvTitle.text = item.reasonType.toString()
                adminTvReporter.text = item.reporterId

                adminHolder.setOnClickListener {

                    itemClickListener(item, position)
                }
                adminHolder.setOnLongClickListener {
                    itemLongClickListener(item, position)
                    true
                }
            }
        }
    }


}

