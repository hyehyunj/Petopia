package com.android.petopia.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.R
import com.android.petopia.databinding.FragmentHomeEarthBinding
import com.android.petopia.presentation.community.CommunityFragment

class HomeEarthFragment : Fragment() {
    private val _binding: FragmentHomeEarthBinding by lazy {
        FragmentHomeEarthBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onClickListener()
    }


    private fun onClickListener() {
        //설정버튼 클릭이벤트 : 클릭시 설정 이동

        binding.homeEarthIvMy.setOnClickListener {

        }

        //버튼 클릭이벤트 : 클릭시 관리자 추천글 배웅하기 이동

        binding.homeEarthIvCloudLeft.setOnClickListener {

        }

        //메모리버튼 클릭이벤트 : 클릭시 관리자 추천글 잘지내기 이동

        binding.homeEarthIvCloudRight.setOnClickListener {

        }

        //메모리버튼 클릭이벤트 : 클릭시 커뮤니티 이동
        binding.homeEarthIvCommunity.setOnClickListener {
        }
    }
    }