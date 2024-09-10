package com.djhb.petopia.presentation.admin

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
import com.djhb.petopia.databinding.FragmentDialogBinding

//계정삭제 다시 확인해주는 다이얼로그
class AdminExileDialogFragment : DialogFragment() {

    private val _binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminViewModel =
            ViewModelProvider(requireActivity()).get(AdminViewModel::class.java)

        binding.apply {
            dialogTvAction.text = "계정삭제"
            dialogTvTitle.text = "신고대상자의 계정을 정말 삭제하시겠습니까?"

            //계정삭제버튼 클릭이벤트
            dialogTvAction.setOnClickListener {

            }
            parentFragmentManager.beginTransaction()
                .remove(AdminReportFragment())
                .commit()
            dismiss()


            //취소버튼 클릭이벤트
            binding.dialogTvCancel.setOnClickListener {
                dismiss()
            }
            return binding.root
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
        params?.height = (deviceWidth * 0.5).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }


}
