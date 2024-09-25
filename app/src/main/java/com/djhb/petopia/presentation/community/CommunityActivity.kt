package com.djhb.petopia.presentation.community

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.R
import com.djhb.petopia.databinding.ActivityCommunityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CommunityActivity : AppCompatActivity() {

    private val binding: ActivityCommunityBinding by lazy {
        ActivityCommunityBinding.inflate(layoutInflater)
    }

    private val viewPagerAdapter: CommunityViewPagerAdapter by lazy {
        CommunityViewPagerAdapter(this)
    }

    private val postTypes = listOf("질문 게시판", "정보 공유 게시판", "사진 게시판")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager = binding.vpCommunity
        viewPager.adapter = viewPagerAdapter
        val tabCommunity = binding.tabCommunity
//        tabCommunity.addOnTabSelectedListener(this@CommunityActivity)

        TabLayoutMediator(tabCommunity, viewPager) { tab, position ->
//            tab.text = "tab + ${position + 1}"
            tab.text = postTypes[position]
        }.attach()

    }

    fun hideViewPager() = with(binding) {
        layoutCommunity.isVisible = true
        vpCommunity.isVisible = false
        tabCommunity.isVisible = false
    }

    fun showViewPager() = with(binding) {
        layoutCommunity.isVisible = false
        vpCommunity.isVisible = true
        tabCommunity.isVisible = true
    }
}