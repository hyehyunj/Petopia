package com.android.petopia.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.petopia.presentation.community.CommunityFragment
import com.android.petopia.presentation.guide.GuideFragment
import com.android.petopia.presentation.home.HomeEarthFragment
import com.android.petopia.presentation.home.HomeMemoryBridgeFragment
import com.android.petopia.presentation.home.HomePetopiaFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
//    private val fragments = listOf(HomePetopiaFragment(), HomeMemoryBridgeFragment(), HomeEarthFragment())
    private val fragments = listOf(HomePetopiaFragment(), HomeMemoryBridgeFragment(), CommunityFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}