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
import androidx.fragment.app.viewModels
import com.djhb.petopia.databinding.FragmentAdminPostBinding
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherView


//관리자 게시글
class AdminPostLeftFragment : DialogFragment() {
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


        binding.adminPostTvTitle.text = "배웅하기"
        adminPostLeftDataObserver()
        adminPostLeftButtonClickListener()
        initAdapter()
        weatherChange("RAIN")

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun adminPostLeftButtonClickListener() {
        //뒤로가기버튼 클릭이벤트 :
        binding.adminPostIvBack.setOnClickListener {
            dismiss()
        }

    }

    private fun weatherChange(weather: String) {
        val weatherView: WeatherView = binding.adminPostWv

        when (weather) {
            "CLEAR" -> {
                weatherView.setWeatherData(PrecipType.CLEAR)
                binding.adminPostBgWeather.setBackgroundColor(Color.WHITE)
            }

            "RAIN" -> {
                weatherView.setWeatherData(PrecipType.RAIN)
            }
        }

    }


    private fun initAdapter() {
        deckPager.apply {
            offscreenPageLimit = 5
            adapter = AdminPostAdapter(requireContext(), adminPostViewModel.getAdminPostLeftList(),
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

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun adminPostLeftDataObserver() {
        adminPostViewModel.adminPostLeftListLiveData.observe(viewLifecycleOwner) { post ->
//    val fcTitleList = listOf(binding.adminPostFc1Title)
//    val fcSubTitleList = listOf()
//        val fcPostList = listOf()
//  for(i in post) {
//  fcPostList[i] = i.title
//
//
//}
        }
    }


    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 1).toInt()
        params?.height = (deviceWidth * 2.1).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onResume() {
        super.onResume()
        initDialog()
    }

}