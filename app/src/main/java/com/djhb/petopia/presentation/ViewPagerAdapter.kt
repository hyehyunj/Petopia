package com.djhb.petopia.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.djhb.petopia.presentation.home.HomeEarthFragment
import com.djhb.petopia.presentation.home.HomeMemoryBridgeFragment
import com.djhb.petopia.presentation.home.HomePetopiaFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(HomePetopiaFragment(), HomeMemoryBridgeFragment(), HomeEarthFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}