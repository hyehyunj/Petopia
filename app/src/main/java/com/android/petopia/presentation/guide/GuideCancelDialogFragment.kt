package com.android.petopia.presentation.guide

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
import com.android.petopia.databinding.FragmentDialogBinding
import com.android.petopia.presentation.home.MainHomeGuideSharedViewModel


class GuideCancelDialogFragment : DialogFragment() {
    private val _binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var homePetopiaGuideSharedViewModel: MainHomeGuideSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homePetopiaGuideSharedViewModel =
            ViewModelProvider(requireParentFragment()).get(MainHomeGuideSharedViewModel::class.java)
        binding.dialogTvAction.text = "종료"

        homePetopiaGuideSharedViewModel.guideStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                "ESSENTIAL" -> binding.dialogTvTitle.text = "다음에 찾아오시겠어요?\n처음부터 진행됩니다."
                "OPTIONAL" -> binding.dialogTvTitle.text = "가이드를 종료하시겠어요?\n마이페이지에서 다시 보실 수 있습니다."
            }
        }

        //종료버튼 클릭이벤트
        binding.dialogTvAction.setOnClickListener {
            when (homePetopiaGuideSharedViewModel.guideStateLiveData.value) {
                "ESSENTIAL" -> homePetopiaGuideSharedViewModel.updateGuideState("NONE")
                "OPTIONAL" -> homePetopiaGuideSharedViewModel.updateGuideState("DONE")
            }
            parentFragmentManager.beginTransaction()
                .remove(GuideFragment())
                .commit()
            dismiss()
        }

        //취소버튼 클릭이벤트
        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }
        return binding.root
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
        params?.height = (deviceWidth * 0.5).toInt()
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
