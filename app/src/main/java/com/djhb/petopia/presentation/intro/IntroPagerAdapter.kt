package com.djhb.petopia.presentation.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import com.djhb.petopia.R
import com.djhb.petopia.data.IntroModel

class IntroPagerAdapter(
    private val context: Context,
    private val items: List<IntroModel>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.pager_intro_item, container, false)
        container?.addView(view, 0)
        if(position == 0 || position == 5) {
            view.findViewById<ImageView>(R.id.intro_item_iv_background).setImageResource(R.drawable.bg_intro_gradient)
        }
        view.findViewById<TextView>(R.id.intro_item_tv_title).text = items[position].introTitle
        view.findViewById<ImageView>(R.id.intro_item_iv_icon).setBackgroundResource(items[position].introImage)
        view.findViewById<TextView>(R.id.intro_item_tv_info).text = items[position].introInfo

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = items.size

}
