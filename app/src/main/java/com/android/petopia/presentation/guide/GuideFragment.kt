package com.android.petopia.presentation.guide

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.petopia.R
import com.android.petopia.databinding.FragmentGuideBinding
import com.android.petopia.databinding.ToastBinding
import com.android.petopia.presentation.home.HomePetopiaFragment
import io.github.muddz.styleabletoast.StyleableToast

//가이드 프래그먼트 : 앱 사용방법과 특징을 안내하는 튜토리얼
class GuideFragment : Fragment() {
    private val _binding: FragmentGuideBinding by lazy {
        FragmentGuideBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private val guideViewModel by viewModels<GuideViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //다음으로 또는 뒤로가기 버튼 클릭에 따라 페이지 번호 변경
        guideViewModel.guidePageNumberLiveData.observe(viewLifecycleOwner) {
            Log.d("가이드는", "${guideViewModel.guidePageNumberLiveData.value}")
            when (it) {
                -1 -> StyleableToast.makeText(requireActivity(), "첫 장면입니다.", R.style.toast_custom)
                    .show()

                in 0..7 -> guideViewModel.makeGuideModel()
                8 -> {
                    guideViewModel.makeGuideModel()
                    (parentFragment as HomePetopiaFragment).showPet()
                    binding.apply {
                        guideTvProgressText.isVisible = false
                        guideIvProgressBar.isVisible = false
                    }
                }

            }
        }

        //페이지 번호에 따라 화면 구성
        guideViewModel.guideModelLiveData.observe(viewLifecycleOwner) {
            //스토리
            binding.guideTvStory.text = it.guideStory
            //상단진행문구
            if (guideViewModel.guidePageNumberLiveData.value!! < 8)
                binding.guideTvProgressText.text =
                    it.progressText
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


        //다음으로 또는 뒤로가기 버튼 클릭이벤트
        binding.guideIvNext.setOnClickListener {
            guideViewModel.guideButtonClickListener("NEXT")
        }
        binding.guideIvBack.setOnClickListener {
            guideViewModel.guideButtonClickListener("BACK")
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //나가기 버튼 클릭이벤트
        binding.guideIvExit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                        .remove(this)
                       .commit()
        }
    }
}






