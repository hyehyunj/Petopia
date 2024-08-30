package com.android.petopia.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.petopia.presentation.community.CommunityFragment
import com.android.petopia.presentation.gallery.GalleryFragment
import com.android.petopia.presentation.guide.GuideFragment
import com.android.petopia.presentation.home.HomeFragment
import com.android.petopia.presentation.my.MyFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(HomeFragment(), CommunityFragment(), GuideFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}