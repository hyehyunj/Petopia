package com.android.petopia.presentation.guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.R
import com.android.petopia.databinding.FragmentGuideBinding
import com.android.petopia.presentation.MainActivity
import com.android.petopia.presentation.home.HomePetopiaFragment
import com.android.petopia.presentation.home.MainHomeGuideSharedViewModel
import io.github.muddz.styleabletoast.StyleableToast

//가이드 프래그먼트 : 앱 사용방법과 특징을 안내하는 튜토리얼
class GuideFragment : Fragment() {
    private val _binding: FragmentGuideBinding by lazy {
        FragmentGuideBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val guideViewModel by viewModels<GuideSharedViewModel>()
    private lateinit var homePetopiaGuideSharedViewModel: MainHomeGuideSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePetopiaGuideSharedViewModel =
            ViewModelProvider(requireActivity())[MainHomeGuideSharedViewModel::class.java]

        guideButtonClickListener()
        guideDataObserver()

    }

    //버튼 클릭이벤트 함수 : 눌린 버튼에 따라 동작해주는 함수
    private fun guideButtonClickListener() {
        //다음으로 버튼 클릭이벤트
        binding.guideIvNext.setOnClickListener {
            guideViewModel.guideButtonClickListener("NEXT")
        }
        //뒤로가기 버튼 클릭이벤트
        binding.guideIvBack.setOnClickListener {
            when (guideViewModel.guidePageNumberLiveData.value) {
                0, 14 -> StyleableToast.makeText(
                    requireActivity(),
                    "첫 장면입니다.",
                    R.style.toast_custom
                ).show()
                13,17 -> binding.guideTvStory.isVisible = true
                else -> guideViewModel.guideButtonClickListener("BACK")
            }
        }
        //나가기 버튼 클릭이벤트
        binding.guideIvExit.setOnClickListener {
            (activity as MainActivity).cancelGuide()
        }

//
//
//
//            when(homePetopiaGuideSharedViewModel.guideStateLiveData.value) {
//                "ESSENTIAL" -> homePetopiaGuideSharedViewModel.updateGuideState("NONE")
//                "OPTIONAL" -> homePetopiaGuideSharedViewModel.updateGuideState("DONE")
//            }
//            parentFragmentManager.beginTransaction()
//                .remove(this)
//                .commit()
//        }
    }

    //데이터 옵저버 함수 : 데이터 변화를 감지해 해당하는 동작을 진행해주는 함수
    private fun guideDataObserver() {
        //페이지 변화감지 : 다음으로 또는 뒤로가기 버튼 클릭에 따라 페이지 번호 변경
        guideViewModel.guidePageNumberLiveData.observe(viewLifecycleOwner) {
            Log.d("가이드는", "${guideViewModel.guidePageNumberLiveData.value}")
            when (it) {

                in 0..7 -> guideViewModel.makeGuideModel()
                8 -> {
                    homePetopiaGuideSharedViewModel.updateGuideState("OPTIONAL")
                    guideViewModel.makeGuideModel()
                    binding.apply {
                        guideTvProgressText.isVisible = false
                        guideIvProgressBar.isVisible = false
                    }
                }

                in 9..12, in 15..16 -> {
                    homePetopiaGuideSharedViewModel.updateFunction(it)
                    guideViewModel.makeGuideModel()
                }

                13, 17 -> {
                    binding.guideTvStory.isVisible = false
                    homePetopiaGuideSharedViewModel.updateFunction(it)
                }

                14 -> {
                    binding.guideTvStory.isVisible = true
                    homePetopiaGuideSharedViewModel.updateFunction(it)
                    guideViewModel.makeGuideModel()
                }

            }
//지구 이동이 안됨


        }

        //가이드모델 변화감지 : 페이지 번호에 따라 화면 구성 변경
        guideViewModel.guideModelLiveData.observe(viewLifecycleOwner) {
            Log.d("기능", "${homePetopiaGuideSharedViewModel.guideFunctionLiveData.value}")
            Log.d("페이지", "")
            //상단진행부
            if (guideViewModel.guidePageNumberLiveData.value!! < 8) {
                binding.guideTvProgressText.text =
                    it.progressText
            }

            //하단스토리
            binding.guideTvStory.text = it.guideStory
            //다이얼로그
            when (guideViewModel.guideModelLiveData.value?.dialog) {
                0 -> GuideNameDialogFragment().apply {
                    isCancelable = false
                }.show(childFragmentManager, "DIALOG_FRAGMENT")

                1 -> GuideAppearanceDialogFragment().apply {
                    isCancelable = false
                }.show(childFragmentManager, "DIALOG_FRAGMENT")

                2 -> GuideRelationDialogFragment().apply {
                    isCancelable = false
                }.show(childFragmentManager, "DIALOG_FRAGMENT")
            }
        }

        homePetopiaGuideSharedViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            if (it == "DONE" || it == "NONE")
                parentFragmentManager.beginTransaction()
                    .remove(this)
                    .commit()
        }
        homePetopiaGuideSharedViewModel.currentHomeLiveData.observe(viewLifecycleOwner) {
            Log.d("메모리", "${guideViewModel.guidePageNumberLiveData.value}")
            if (it == 1 || it == 2) guideViewModel.guideButtonClickListener("NEXT")

        }

    }
}






