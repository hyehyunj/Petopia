package com.djhb.petopia.presentation.guide

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentGuideRelationDialogBinding
import io.github.muddz.styleabletoast.StyleableToast

//다이얼로그 프래그먼트 : 전역에서 사용되는 다이얼로그
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
            ViewModelProvider(requireParentFragment()).get(GuideSharedViewModel::class.java)


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
            } else StyleableToast.makeText(requireActivity(), "이미지를 선택해주세요", R.style.toast_custom)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()

        //다이얼로그 사용자 폰에 맞춰 크기조정, 리팩토링 필요
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
        params?.height = (deviceWidth * 0.6).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//            requireContext().dialogFragmentResize(this, 0.9f, 0.8f)
//        }
//        private fun Context.dialogFragmentResize(
//            dialogFragment: DialogFragment,
//            width: Float,
//            height: Float,
//        ) {
//            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            if (Build.VERSION.SDK_INT < 30) {
//                val display = windowManager.defaultDisplay
//                val size = Point()
//                display.getSize(size)
//                val window = dialogFragment.dialog?.window
//                val x = (size.x * width).toInt()
//                val y = (size.y * height).toInt()
//                window?.setLayout(x, y)
//            } else {
//                val rect = windowManager.currentWindowMetrics.bounds
//                val window = dialogFragment.dialog?.window
//                val x = (rect.width() * width).toInt()
//                val y = (rect.height() * height).toInt()
//                window?.setLayout(x, y)
//            }
    }


}
