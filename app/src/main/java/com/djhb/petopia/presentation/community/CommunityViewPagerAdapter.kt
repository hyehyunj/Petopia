package com.djhb.petopia.presentation.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.djhb.petopia.Table

class CommunityViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        CommunityMainFragment.newInstance(Table.QUESTION_POST),
        CommunityMainFragment.newInstance(Table.INFORMATION_POST),
        CommunityMainFragment.newInstance(Table.GALLERY_POST)
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}