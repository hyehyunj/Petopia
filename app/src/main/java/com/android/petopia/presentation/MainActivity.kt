package com.android.petopia.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.android.petopia.R
import com.android.petopia.databinding.ActivityMainBinding
import com.android.petopia.presentation.dialog.DialogFragment
import com.android.petopia.presentation.gallery.PhotoFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mainSharedViewModel: MainSharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//액티비티 공유 뷰모델 갖다 쓰세요~!
        //private val sharedViewModel : MainViewModel by activityViewModels()


        //레이아웃 초기화
        initLayout()
        mainSharedViewModel = ViewModelProvider(this)[MainSharedViewModel::class.java]


    }

    //레이아웃 초기화 함수 : 뷰페이저, 탭레이아웃 연결
    private fun initLayout() {
        val viewPager = binding.mainViewPager
        val mainViewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = mainViewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.common_petopia)
                }

                1 -> {
                    tab.text = getString(R.string.common_memory_bridge)
                }

                2 -> {
                    tab.text = getString(R.string.common_earth)
                }
            }
        }.attach()
    }


    //이 3줄 추가하면 다이얼로그! 버튼이벤트.setOnClickListener {
    //        (activity as MainActivity).showDialog()
    //    }
//다이얼로그 띄우는 함수
    fun showDialog() {
        DialogFragment().show(supportFragmentManager, "DIALOG_FRAGMENT")


//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_signin_container, DialogFragment()
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()
    }




}