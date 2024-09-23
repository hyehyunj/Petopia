package com.djhb.petopia.presentation.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    private val _binding: FragmentIntroBinding by lazy {
        FragmentIntroBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val introViewModel by viewModels<IntroViewModel> {
        IntroViewModelFactory()
    }
    private val introViewPager by lazy { binding.introPager }
    private lateinit var introViewPagerAdapter: IntroPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIntroViewPagerAdapter()
    }

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
                    0, 4 -> binding.introBackground.setBackgroundResource(R.drawable.img_login_cloud)
                    1 -> binding.introBackground.setBackgroundResource(R.drawable.img_home_background)
                    2 ->binding.introBackground.setBackgroundResource(R.drawable.bridge)
                    3 ->binding.introBackground.setBackgroundResource(R.drawable.earth_background)
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }





    override fun onResume() {
        super.onResume()

    }

}