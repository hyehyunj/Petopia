package com.djhb.petopia.presentation.guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentGuideBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import io.github.muddz.styleabletoast.StyleableToast

//가이드 프래그먼트 : 앱 사용방법과 특징을 안내하는 튜토리얼
class GuideFragment : Fragment() {
    private val _binding: FragmentGuideBinding by lazy {
        FragmentGuideBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val guideViewModel by viewModels<GuideSharedViewModel> {
        GuideSharedViewModelFactory()
    }
    private lateinit var mainHomeGuideSharedViewModel: MainHomeGuideSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainHomeGuideSharedViewModel =
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
        binding.guideStoryLayout.setOnClickListener {
            guideViewModel.guideButtonClickListener("NEXT")
        }
        //뒤로가기 버튼 클릭이벤트
        binding.guideIvBack.setOnClickListener {
            when (guideViewModel.guidePageNumberLiveData.value) {
                0, 7, 8, 14, 18 -> StyleableToast.makeText(
                    requireActivity(),
                    "더 이상 뒤로가실 수 없습니다.",
                    R.style.toast_custom
                ).show()
                13, 17 -> StyleableToast.makeText(
                    requireActivity(),
                    "아래로 이동해보세요!",
                    R.style.toast_custom
                ).show()
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
            Log.d("페이지", "${guideViewModel.guidePageNumberLiveData.value}")
            when (it) {

                in 0..7 -> { guideViewModel.makeGuideModel()
                }
                8 -> {
                    mainHomeGuideSharedViewModel.updateGuideState("OPTIONAL")
                    guideViewModel.makeGuideModel()
                    mainHomeGuideSharedViewModel.getPetData()
                    binding.apply {
                        guideTvProgressText.isVisible = false
                        guideIvProgressBar.isVisible = false
                    }
                }

                in 9..12, in 15..16, in 19..22 -> {
                    mainHomeGuideSharedViewModel.updateFunction(it)
                    guideViewModel.makeGuideModel()
                }

                13, 17 -> {
                    binding.guideStoryLayout.isVisible = false
                    mainHomeGuideSharedViewModel.updateFunction(it)
                }

                14, 18 -> {
                    binding.guideStoryLayout.isVisible = true
                    mainHomeGuideSharedViewModel.updateFunction(it)
                    guideViewModel.makeGuideModel()
                }

                23 -> {
                    mainHomeGuideSharedViewModel.updateGuideState("DONE")
                }
            }
        }

        //가이드모델 변화감지 : 페이지 번호에 따라 화면 구성 변경
        guideViewModel.guideModelLiveData.observe(viewLifecycleOwner) {
            Log.d("기능", "${mainHomeGuideSharedViewModel.guideFunctionLiveData.value}")
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

                1 -> com.djhb.petopia.presentation.guide.GuideAppearanceDialogFragment().apply {
                    isCancelable = false
                }.show(childFragmentManager, "DIALOG_FRAGMENT")

                2 -> GuideRelationDialogFragment().apply {
                    isCancelable = false
                }.show(childFragmentManager, "DIALOG_FRAGMENT")
            }
        }

        mainHomeGuideSharedViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            if (it == "DONE" || it == "NONE")
                parentFragmentManager.beginTransaction()
                    .remove(this)
                    .commit()
        }
        mainHomeGuideSharedViewModel.currentHomeLiveData.observe(viewLifecycleOwner) {
            Log.d("메모리", "${guideViewModel.guidePageNumberLiveData.value}")
            if (it == 1 || it == 2) guideViewModel.guideButtonClickListener("NEXT")

        }

    }
}






