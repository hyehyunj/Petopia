package com.djhb.petopia.presentation.admin.post

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.djhb.petopia.databinding.FragmentAdminPostBinding
import com.djhb.petopia.presentation.MainActivity
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView

//안녕하기 프래그먼트 : 우측 구름을 클릭하면 볼 수 있는 관리자 게시글
class AdminPostRightFragment : Fragment() {
    private val _binding: FragmentAdminPostBinding by lazy {
        FragmentAdminPostBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private val adminPostViewModel by viewModels<AdminPostViewModel> {
        AdminPostViewModelFactory()
    }
    private val deckPager by lazy { binding.adminPostDeck }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adminPostTvTitle.text = "안녕하기"
        initAdapter()
        adminPostLeftButtonClickListener()
        weatherChange("SNOW")
    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun adminPostLeftButtonClickListener() {
        //뒤로가기버튼 클릭이벤트 : 클릭시 프래그먼트 제거
        binding.adminPostIvBack.setOnClickListener {
            (activity as MainActivity).removeAdminPostFragment()
        }
    }

    //배경날씨 변경해주는 함수
    private fun weatherChange(weather: String) {
        val weatherView: WeatherView = binding.adminPostWv

        when (weather) {
            "CLEAR" -> {
                weatherView.setWeatherData(PrecipType.CLEAR)
                binding.adminPostBgWeather.setBackgroundColor(Color.WHITE)
            }

            "SNOW" -> {
                weatherView.setWeatherData(PrecipType.SNOW)
            }
        }

    }

    //게시글 구성해주는 함수
    private fun initAdapter() {
        deckPager.apply {
            offscreenPageLimit = 5
            adapter = AdminPostAdapter(requireContext(), adminPostViewModel.getAdminPostRightList(),
                itemClickListener = { item, position ->
                    binding.adminPostTvPost.apply {
                        isVisible = true
                        text = item.post
                    }
                    binding.adminPostIvGrass.isVisible = true
                    binding.adminPostTvNonPost.isVisible = false
                    weatherChange("CLEAR")

                }
            )
            clipToPadding = false
            setPadding(180, 0, 180, 0)
            pageMargin = 30


        }
    }

}