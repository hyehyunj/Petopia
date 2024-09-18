package com.djhb.petopia.presentation.admin.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.djhb.petopia.R
import shivam.developer.featuredrecyclerview.FeatureRecyclerViewAdapter

class AdminPostFeatureRecyclerViewAdapter : FeatureRecyclerViewAdapter<RecyclerView.ViewHolder>() {

        private val ITEM_TYPE_FEATURED = 0
        private val ITEM_TYPE_DUMMY = 1

        private var data: List<String> = emptyList()


        fun swapData(data: List<String>) {
            this.data = data
            notifyDataSetChanged()
        }

        override fun onCreateFeaturedViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ITEM_TYPE_FEATURED -> FeaturedViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_featured_item, parent, false)
                )
                ITEM_TYPE_DUMMY -> DummyViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_dummy_item, parent, false)
                )
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindFeaturedViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is FeaturedViewHolder -> {
                    holder.tvHeading.text = data[position]
                }
                is DummyViewHolder -> {
                    // Do nothing
                }
            }
        }

        override fun getFeaturedItemsCount(): Int {
            return data.size + 2 // Return 2 extra dummy items
        }

        override fun getItemViewType(position: Int): Int {
            return if (position in 0 until data.size) {
                ITEM_TYPE_FEATURED
            } else {
                ITEM_TYPE_DUMMY
            }
        }

        override fun onSmallItemResize(holder: RecyclerView.ViewHolder, position: Int, offset: Float) {
            if (holder is FeaturedViewHolder) {
                holder.tvHeading.alpha = offset / 100f
            }
        }

        override fun onBigItemResize(holder: RecyclerView.ViewHolder, position: Int, offset: Float) {
            if (holder is FeaturedViewHolder) {
                holder.tvHeading.alpha = offset / 100f
            }
        }

        class FeaturedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvHeading: TextView = itemView.findViewById(R.id.tv_heading)
        }

        class DummyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }