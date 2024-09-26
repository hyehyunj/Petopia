package com.djhb.petopia.presentation.community

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val builder = AlertDialog.Builder(this@CommunityActivity)
            builder.setTitle("앱 종료")
            builder.setMessage("정말 나가시겠습니까?")
            builder.setIcon(R.drawable.icon_heart)

            builder.setPositiveButton("확인") {dialog, which ->
                when(which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        finish()
                    }
                }
            }

            builder.setNegativeButton("취소", null)
            builder.show()
        }
    }

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

//        this.onBackPressedDispatcher.addCallback(backPressedCallback)

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