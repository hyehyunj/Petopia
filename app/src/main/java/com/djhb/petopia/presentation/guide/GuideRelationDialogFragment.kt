package com.djhb.petopia.presentation.guide

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentGuideRelationDialogBinding
import io.github.muddz.styleabletoast.StyleableToast

//반려동물 관계 다이얼로그 프래그먼트 : 사용자와 반려동물의 관계를 입력받는 다이얼로그
class GuideRelationDialogFragment : DialogFragment() {
    private val _binding: FragmentGuideRelationDialogBinding by lazy {
        FragmentGuideRelationDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var guideSharedViewModel: GuideSharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        guideSharedViewModel =
            ViewModelProvider(requireParentFragment())[GuideSharedViewModel::class.java]

        //값을 담아주는 클릭 이벤트
        binding.guideRelationDialogRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.guide_relation_dialog_rb_child -> guideSharedViewModel.setPetRelation("CHILD")
                R.id.guide_relation_dialog_rb_younger -> guideSharedViewModel.setPetRelation("YOUNGER")
                R.id.guide_relation_dialog_rb_friend -> guideSharedViewModel.setPetRelation("FRIEND")
            }
        }


        //이전으로버튼 클릭이벤트
        binding.guideRelationDialogTvBack.setOnClickListener {
            guideSharedViewModel.guideButtonClickListener("BACK")
            dismiss()
        }

        //완료버튼 클릭이벤트
        binding.guideRelationDialogTvComplete.setOnClickListener {
            if (guideSharedViewModel.preparedPetData(2)) {
                guideSharedViewModel.guideButtonClickListener("NEXT")
                dismiss()
            } else StyleableToast.makeText(requireActivity(), "이미지를 선택해주세요", R.style.toast_common)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}
