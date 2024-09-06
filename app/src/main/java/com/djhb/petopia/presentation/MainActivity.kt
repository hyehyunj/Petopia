package com.djhb.petopia.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.R
import com.djhb.petopia.databinding.ActivityMainBinding
import com.djhb.petopia.presentation.dialog.DialogFragment
import com.djhb.petopia.presentation.guide.GuideCancelDialogFragment
import com.djhb.petopia.presentation.guide.GuideFragment
import com.djhb.petopia.presentation.guide.GuideSharedViewModel
import com.djhb.petopia.presentation.guide.GuideSharedViewModelFactory
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mainDialogSharedViewModel: MainDialogSharedViewModel
    private val mainHomeGuideSharedViewModel by viewModels<MainHomeGuideSharedViewModel> {
        MainHomeGuideSharedViewModelFactory()
    }
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//액티비티 공유 뷰모델 갖다 쓰세요~!
        //private val sharedViewModel : MainViewModel by activityViewModels()
        mainHomeGuideSharedViewModel.guideStateLiveData.observe(this) {
            Log.d("액티비티 종료?", "${it}")
            when (it) {
                "DONE" -> {
                    finishGuideFragment()
                }
                "ESSENTIAL" -> binding.mainViewPager.isUserInputEnabled = false
            }
        }

        mainHomeGuideSharedViewModel.guideFunctionLiveData.observe(this) {
            Log.d("지금 프래그먼트는", "${mainHomeGuideSharedViewModel.currentHomeLiveData.value}")
//            binding.mainViewPager.isUserInputEnabled = false
            when (it) {
                "MOVE_MEMORY_BRIDGE", "MOVE_EARTH"-> {
                    binding.mainViewPager.isUserInputEnabled = true
                    viewPager.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            mainHomeGuideSharedViewModel.updateCurrentHome(position)
                        }
                    })
                }
            }

        }


        //레이아웃 초기화
        initLayout()
    }

    //레이아웃 초기화 함수 : 뷰페이저, 탭레이아웃 연결
    private fun initLayout() {
        viewPager = binding.mainViewPager
        val mainViewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = mainViewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    fun cancelGuide() {

//            supportFragmentManager.beginTransaction()
//            .replace(R.id.main_signin_container, DialogFragment()
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()
        GuideCancelDialogFragment().show(supportFragmentManager, "GUIDE_CANCEL_DIALOG_FRAGMENT")

    }
//    fun clearGuide() {
//        binding.mainViewPager.isUserInputEnabled = true
//        supportFragmentManager.beginTransaction()
//            .remove(GuideFragment())
//            .commit()
//    }
    //이 3줄 추가하면 다이얼로그! 버튼이벤트.setOnClickListener {
    //        (activity as MainActivity).showDialog()
    //    }
//다이얼로그 띄우는 함수
    fun showDialog() {
        mainDialogSharedViewModel = ViewModelProvider(this)[MainDialogSharedViewModel::class.java]
//        mainDialogSharedViewModel.updateDialogMode(mode)
        DialogFragment().show(supportFragmentManager, "DIALOG_FRAGMENT")


//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_signin_container, DialogFragment()
//            )
//            .setReorderingAllowed(true)
//            .addToBackStack(null)
//            .commit()
    }


    fun showGuideFragment() {
        mainHomeGuideSharedViewModel.updateGuideState("ESSENTIAL")
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_sub_frame, GuideFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

    }

    //가이드 완료 함수 : 가이드 완료 후 펫토피아로 이동
    private fun finishGuideFragment() {
        binding.mainViewPager.isUserInputEnabled = true
        moveToPetopia()
        supportFragmentManager.beginTransaction()
            .remove(GuideFragment())
            .commit()

    }

    fun moveToPetopia() {
        viewPager.setCurrentItem(0, true)
    }


    fun hideViewPager() = with(binding) {
        mainSubFrame.isVisible = true
        viewPager.isVisible = false
    }

    fun showViewPager() = with(binding) {
        mainSubFrame.isVisible = false
        viewPager.isVisible = true
    }


}