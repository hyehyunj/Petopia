package com.android.petopia.presentation.guide

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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.android.petopia.databinding.FragmentGuideNameDialogBinding

//가이드 반려동물 이름 다이얼로그 프래그먼트 : 가이드 진행 중 반려동물 이름을 입력받을 때 사용되는 다이얼로그
class GuideNameDialogFragment : DialogFragment() {
    private val _binding: FragmentGuideNameDialogBinding by lazy {
        FragmentGuideNameDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var guideSharedViewModel: GuideViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        guideSharedViewModel = ViewModelProvider(requireParentFragment()).get(GuideViewModel::class.java)

        Log.d("네임은", "${guideSharedViewModel.guidePageNumberLiveData.value}")

        binding.dialogNameTvComplete.setOnClickListener {

           if(binding.dialogNameEt.text.isBlank()) Toast.makeText(requireContext(),"이름을 입력해주세요.", Toast.LENGTH_SHORT).show()

               else {guideSharedViewModel.setPetName(binding.dialogNameEt.text.toString())
            Log.d("바뀝니다", "${guideSharedViewModel.guidePageNumberLiveData.value}")
            dismiss()}

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
        params?.width = (deviceWidth * 0.8).toInt()
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
