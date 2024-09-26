package com.djhb.petopia.presentation.my

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
import com.djhb.petopia.databinding.FragmentDialogBinding
import com.djhb.petopia.presentation.MainActivity
import io.github.muddz.styleabletoast.StyleableToast

//다이얼로그 프래그먼트
open class MyGuideDialogFragment : DialogFragment() {
    private val _binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.dialogTvAction.text = "네"
        binding.dialogTvTitle.text = "가이드를 진행하시겠습니까?"



        binding.dialogTvAction.setOnClickListener {
            StyleableToast.makeText(requireActivity(), "이제 펫토피아에서 연결이 가능합니다.", R.style.toast_warning)
                .show()
            (activity as MainActivity).welcomeGuide()

        }


        //취소버튼 클릭이벤트
        binding.dialogTvCancel.setOnClickListener {
            dismiss()
        }


        return binding.root
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
        params?.height = (deviceWidth * 0.5).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}
