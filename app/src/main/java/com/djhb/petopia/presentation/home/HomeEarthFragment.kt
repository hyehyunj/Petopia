package com.djhb.petopia.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.databinding.FragmentHomeEarthBinding

class HomeEarthFragment : Fragment() {
    private val _binding: FragmentHomeEarthBinding by lazy {
        FragmentHomeEarthBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var mainHomeGuideViewModel: MainHomeGuideSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainHomeGuideViewModel =
            ViewModelProvider(requireActivity()).get(MainHomeGuideSharedViewModel::class.java)

        homeEarthButtonClickListener()
        homeEarthDataObserver()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun homeEarthButtonClickListener() {
        //설정버튼 클릭이벤트 : 클릭시 설정 이동
        binding.homeEarthIvMy.setOnClickListener {

        }

        //좌측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 배웅하기 이동
        binding.homeEarthIvCloudLeft.setOnClickListener {

        }

        //우측구름버튼 클릭이벤트 : 클릭시 관리자 추천글 잘지내기 이동
        binding.homeEarthIvCloudRight.setOnClickListener {

        }

        //메모리버튼 클릭이벤트 : 클릭시 커뮤니티 이동
        binding.homeEarthIvCommunity.setOnClickListener {
        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun homeEarthDataObserver() {
        //가이드 상태 변화감지 : 가이드 상태에 따라 화면구성 변경
        mainHomeGuideViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "OPTIONAL" -> binding.homeEarthFabTop.isVisible = false
                "NONE", "DONE" -> binding.homeEarthFabTop.isVisible = true
            }
        }

        mainHomeGuideViewModel.guideFunctionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "MOVE_UPPER" -> binding.homeEarthFabTop.isVisible = true
            }
        }

    }
    }