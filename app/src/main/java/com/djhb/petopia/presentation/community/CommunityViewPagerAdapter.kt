package com.djhb.petopia.presentation.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.djhb.petopia.Table

class CommunityViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        CommunityMainFragment.newInstance(Table.QUESTION_POST.tableName),
        CommunityMainFragment.newInstance(Table.INFORMATION_POST.tableName),
        CommunityMainFragment.newInstance(Table.GALLERY_POST.tableName)
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}