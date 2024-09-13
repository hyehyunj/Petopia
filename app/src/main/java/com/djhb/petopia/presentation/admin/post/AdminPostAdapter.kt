package com.djhb.petopia.presentation.admin.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.viewpager.widget.PagerAdapter
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentAdminPostLeftBinding

class AdminPostAdapter(private val context: Context) : PagerAdapter() {

    private val cards by lazy { genCards() }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card, container, false)
        container?.addView(view, 0)
        view.findViewById<TextView>(R.id.title).apply {
        setBackgroundResource(cards[position].color)
        text = cards[position].name
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = cards.size
}