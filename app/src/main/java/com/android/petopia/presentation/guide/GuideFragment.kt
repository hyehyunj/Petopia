package com.android.petopia.presentation.guide

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.android.petopia.databinding.FragmentGuideBinding
import com.android.petopia.databinding.ToastBinding

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
            if (it in 0..7) guideViewModel.makeGuideModel() else showToastMessage()

        }

        //페이지 번호에 따라 화면 구성
        guideViewModel.guideModelLiveData.observe(viewLifecycleOwner) {

            binding.guideTvStory.text = guideViewModel.guideModelLiveData.value?.guideStory
            binding.guideTvProgressText.text = guideViewModel.guideModelLiveData.value?.progressText
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


    fun showToastMessage() {
        val toastBinding = ToastBinding.inflate(layoutInflater)
        toastBinding.run {
            val toastOnCreateView = Toast(requireActivity())
            toastOnCreateView.apply {
                view = toastBinding.root
                setGravity(Gravity.CENTER, 0, 400)
                duration = Toast.LENGTH_SHORT
                show()
            }
        }
    }

}