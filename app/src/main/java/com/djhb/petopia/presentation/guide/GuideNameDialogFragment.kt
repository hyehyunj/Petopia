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
import com.djhb.petopia.databinding.FragmentGuideNameDialogBinding
import io.github.muddz.styleabletoast.StyleableToast

//가이드 반려동물 이름 다이얼로그 프래그먼트 : 가이드 진행 중 반려동물 이름을 입력받을 때 사용되는 다이얼로그
class GuideNameDialogFragment : DialogFragment() {
    private val _binding: FragmentGuideNameDialogBinding by lazy {
        FragmentGuideNameDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var guideSharedViewModel: GuideSharedViewModel

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

        guideSharedViewModel = ViewModelProvider(requireParentFragment()).get(GuideSharedViewModel::class.java)

      
        //이전으로버튼 클릭이벤트
        binding.dialogNameTvBack.setOnClickListener {
            guideSharedViewModel.guideButtonClickListener("BACK")
        dismiss()
        }



        //완료버튼 클릭이벤트
        binding.dialogNameTvComplete.setOnClickListener {

           if(binding.dialogNameEt.text.isBlank()) StyleableToast.makeText(requireActivity(), "이름을 입력해주세요", R.style.toast_warning).show()

           else { guideSharedViewModel.setPetName(binding.dialogNameEt.text.toString())
            dismiss()}

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
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = (deviceWidth * 0.5).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
