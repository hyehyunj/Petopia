package com.djhb.petopia.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.djhb.petopia.R
import com.djhb.petopia.databinding.ActivityMainBinding
import com.djhb.petopia.presentation.admin.AdminExileDialogFragment
import com.djhb.petopia.presentation.admin.post.AdminPostLeftFragment
import com.djhb.petopia.presentation.admin.post.AdminPostRightFragment
import com.djhb.petopia.presentation.album.AlbumFragment
import com.djhb.petopia.presentation.dialog.DialogFragment
import com.djhb.petopia.presentation.guide.GuideCancelDialogFragment
import com.djhb.petopia.presentation.guide.GuideFragment
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModelFactory
import com.djhb.petopia.presentation.intro.IntroFragment
import com.djhb.petopia.presentation.intro.IntroViewModel
import com.djhb.petopia.presentation.intro.IntroViewModelFactory
import com.djhb.petopia.presentation.my.MyFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mainDialogSharedViewModel: MainDialogSharedViewModel
    private val mainHomeGuideSharedViewModel by viewModels<MainHomeGuideSharedViewModel> {
        MainHomeGuideSharedViewModelFactory()
    }
    private val introViewModel by viewModels<IntroViewModel> {
        IntroViewModelFactory()
    }
    private lateinit var viewPager: ViewPager2
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
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




        mainDataObserver()
        if(introViewModel.loadIntroSkipData(this)) else showIntroFragment()

        //레이아웃 초기화
        initLayout()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    private fun mainDataObserver(){
        mainHomeGuideSharedViewModel.guideStateLiveData.observe(this) {
            when (it) {
                "NONE" -> binding.mainViewPager.isUserInputEnabled = true
                "ESSENTIAL", "ESSENTIAL_DONE", "OPTIONAL" -> binding.mainViewPager.isUserInputEnabled =
                    false

                "DONE" -> finishGuideFragment()
            }
        }

        mainHomeGuideSharedViewModel.guideFunctionLiveData.observe(this) {
            when (it) {
                "MOVE_MEMORY_BRIDGE", "MOVE_EARTH" -> {
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

    }

    //레이아웃 초기화 함수 : 뷰페이저, 탭레이아웃 연결
    private fun initLayout() {
        viewPager = binding.mainViewPager
        val mainViewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = mainViewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    //가이드 취소 다이얼로그를 띄워주는 함수
    fun cancelGuide() {
        GuideCancelDialogFragment().show(supportFragmentManager, "GUIDE_CANCEL_DIALOG_FRAGMENT")
    }

    fun exileUser() {
        AdminExileDialogFragment().show(supportFragmentManager, "ADMIN_EXILE_DIALOG_FRAGMENT")
    }

    //가이드 다시보여줄 준비하는 함수
    fun welcomeGuide() {
        mainHomeGuideSharedViewModel.updateGuideState("NONE")
    }

    //가이드 다시 보여주는 함수
    fun showGuideFragment() {
        mainHomeGuideSharedViewModel.updateGuideState("ESSENTIAL")
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_guide_frame, GuideFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

    }

    //마이페이지에서 가이드 다시 보여주는 함수
    fun showWelcomeGuideFragment() {
        mainHomeGuideSharedViewModel.updateGuideState("OPTIONAL")
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_guide_frame, GuideFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()

    }

    //앨범 프래그먼트 호출해주는 함수
    fun showAlbumFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_Intro_frame, AlbumFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    //앨범 프래그먼트 제거해주는 함수
    fun removeAlbumFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_Intro_frame)
        if (fragment is AlbumFragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.search_recommend_exit,R.anim.search_recommend_exit)
                .remove(fragment)
                .commit()
        }
    }
    //마이 프래그먼트 호출해주는 함수
    fun showMyFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_Intro_frame, MyFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    //인트로 프래그먼트 호출해주는 함수
    fun showIntroFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_Intro_frame, IntroFragment()
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }
    //인트로 프래그먼트 제거해주는 함수
    fun removeIntroFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_Intro_frame)
        if (fragment is IntroFragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.disappear,R.anim.disappear)
                .remove(fragment)
                .commit()
        }
    }

    //관리자게시글 프래그먼트 호출해주는 함수
    fun showAdminPostFragment(cloud: String) {
        when(cloud) {
            "LEFT" -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_Intro_frame, AdminPostLeftFragment()
                )
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
            "RIGHT" -> supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_Intro_frame, AdminPostRightFragment()
                )
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
    //관리자게시글 프래그먼트 제거해주는 함수
    fun removeAdminPostFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_Intro_frame)
        when (fragment) {
            is AdminPostLeftFragment, is AdminPostRightFragment -> {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }


    //가이드 완료 함수 : 가이드 완료 후 펫토피아로 이동
    private fun finishGuideFragment() {
        moveToPetopia()
        binding.mainViewPager.isUserInputEnabled = true
        val fragment = supportFragmentManager.findFragmentById(R.id.main_guide_frame)
        if (fragment is GuideFragment) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }

    }

    //펫토피아로 이동해주는 함수
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