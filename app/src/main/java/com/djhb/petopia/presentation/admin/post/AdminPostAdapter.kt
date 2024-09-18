package com.djhb.petopia.presentation.admin.post

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.PagerAdapter
import com.djhb.petopia.R
import com.djhb.petopia.data.AdminPostModel
import com.djhb.petopia.data.GalleryModel

class AdminPostAdapter(private val context: Context, private val items: List<AdminPostModel>, private val itemClickListener: (item: AdminPostModel, position: Int) -> Unit) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.pager_admin_post_item, container, false)
        container?.addView(view, 0)
        view.findViewById<TextView>(R.id.admin_post_item_tv_title).text = items[position].title
        view.findViewById<ImageView>(R.id.admin_post_item_iv_background).setBackgroundResource(items[position].background)
        view.findViewById<TextView>(R.id.admin_post_item_btn_more).setOnClickListener {
            itemClickListener(items[position], position)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = items.size
}