package com.djhb.petopia.presentation.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.djhb.petopia.databinding.PagerPostDetailImageBinding

class DetailImagePagerAdapter(val context: Context, val imageUris: MutableList<String>): PagerAdapter() {

    override fun getCount(): Int {
        return imageUris.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        val view = inflater.inflate(R.layout.pager_post_detail_image, container, false)

        val binding = PagerPostDetailImageBinding.inflate(inflater)

        Glide.with(binding.ivPostDetail.context)
            .load(imageUris[position].toUri())
            .centerCrop()
            .into(binding.ivPostDetail)

//        return super.instantiateItem(container, position)
        return binding.root
    }
}