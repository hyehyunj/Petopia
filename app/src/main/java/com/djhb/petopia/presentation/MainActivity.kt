package com.djhb.petopia.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.R
import com.djhb.petopia.databinding.ActivityMainBinding
import com.djhb.petopia.presentation.admin.AdminExileDialogFragment
import com.djhb.petopia.presentation.dialog.DialogFragment
import com.djhb.petopia.presentation.guide.GuideCancelDialogFragment
import com.djhb.petopia.presentation.guide.GuideFragment
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mainDialogSharedViewModel: MainDialogSharedViewModel
    private val mainHomeGuideSharedViewModel by viewModels<MainHomeGuideSharedViewModel> {
        MainHomeGuideSharedViewModelFactory()
    }
    private lateinit var viewPager: ViewPager2

    private val onBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.i("MainActivity", "press backButton")
            showViewPager()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mainHomeGuideSharedViewModel.guideStateLiveData.observe(this) {
            when (it) {
                "NONE" -> binding.mainViewPager.isUserInputEnabled = true
                "ESSENTIAL","ESSENTIAL_DONE","OPTIONAL" -> binding.mainViewPager.isUserInputEnabled = false
                "DONE" -> finishGuideFragment()
            }
        }

        mainHomeGuideSharedViewModel.guideFunctionLiveData.observe(this) {
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
                "MEMORY_BRIDGE", "CLOUD" -> binding.mainViewPager.isUserInputEnabled = false


            }

        }


        //레이아웃 초기화
        initLayout()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    //레이아웃 초기화 함수 : 뷰페이저, 탭레이아웃 연결
    private fun initLayout() {
        viewPager = binding.mainViewPager
        val mainViewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = mainViewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    fun cancelGuide() {
        GuideCancelDialogFragment().show(supportFragmentManager, "GUIDE_CANCEL_DIALOG_FRAGMENT")
    }
    fun exileUser() {
        AdminExileDialogFragment().show(supportFragmentManager, "ADMIN_EXILE_DIALOG_FRAGMENT")
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