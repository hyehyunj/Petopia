package com.djhb.petopia.presentation.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//앨범의 각 페이지를 나타내주는 어댑터
class AlbumViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    val listOf = listOf(AlbumMainFragment(), AlbumListFragment())
    private val fragments = listOf

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}