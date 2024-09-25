package com.djhb.petopia.presentation.intro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentIntroBinding
import com.djhb.petopia.presentation.MainActivity

//인트로 프래그먼트 : 어플 소개글
class IntroFragment : Fragment() {
    private val _binding: FragmentIntroBinding by lazy {
        FragmentIntroBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var introViewModel: IntroViewModel
    private val introViewPager by lazy { binding.introPager }
    private lateinit var introViewPagerAdapter: IntroPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        introViewModel =
            ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIntroViewPagerAdapter()
        introButtonClickListener()
    }

    //소개글을 화면에 구성해주는 함수
    private fun initIntroViewPagerAdapter() {
        introViewPager.apply {
            offscreenPageLimit = 5
            introViewPagerAdapter = IntroPagerAdapter(requireActivity(), introViewModel.loadInitIntroList())
            adapter = introViewPagerAdapter
            pageMargin = 30
            setPadding(50, 100, 50, 100)
        }
        val introIndicator = binding.introIndicator
        introIndicator.setViewPager(introViewPager)

        introViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                when (introViewPager.currentItem) {
                    0 -> binding.introBackground.setBackgroundResource(R.drawable.img_login_cloud)
                    1 -> binding.introBackground.setBackgroundResource(R.drawable.img_home_background)
                    2 ->binding.introBackground.setBackgroundResource(R.drawable.bridge)
                    3 ->binding.introBackground.setBackgroundResource(R.drawable.earth_background)
                    4 -> {binding.introBackground.setBackgroundResource(R.drawable.img_petopia_call)
                        binding.introIndicator.isVisible = true
                        binding.introBtnComplete.isVisible = false}
                    5 -> {binding.introBackground.setBackgroundResource(R.drawable.img_login_cloud)
                        binding.introIndicator.isVisible = false
                        binding.introBtnComplete.isVisible = true}
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

//시작하기 버튼클릭이벤트
private fun introButtonClickListener() {
    binding.introBtnComplete.setOnClickListener {
        introViewModel.updateIntroSkipData(requireActivity())
        (activity as MainActivity).removeIntroFragment()
    }
}


}