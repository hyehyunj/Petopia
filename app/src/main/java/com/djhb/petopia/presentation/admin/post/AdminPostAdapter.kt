package com.djhb.petopia.presentation.admin.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.djhb.petopia.R
import com.djhb.petopia.data.AdminPostModel

class AdminPostAdapter(private val context: Context, private val items: List<AdminPostModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.pager_admin_post_item, container, false)
        container?.addView(view, 0)
//        view.findViewById<TextView>(R.id.admin_post_item_tv_page).text = items[position].page
        view.findViewById<TextView>(R.id.admin_post_item_tv_title).text = items[position].title
        view.findViewById<TextView>(R.id.admin_post_item_tv_sub_title).text = items[position].subTitle

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = items.size
}