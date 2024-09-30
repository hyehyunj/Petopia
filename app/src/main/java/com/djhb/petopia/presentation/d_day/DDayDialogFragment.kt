package com.djhb.petopia.presentation.d_day

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
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
open class DDayDialogFragment : DialogFragment() {
    private val _binding: FragmentDialogBinding by lazy {
        FragmentDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.dialogTvAction.text = "네"
        binding.dialogTvTitle.text = "알람 권한을 설정하시겠습니까?"


        binding.dialogTvAction.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
            }
            startActivity(intent)
            dismiss()

        }


        //취소버튼 클릭이벤트
        binding.dialogTvCancel.setOnClickListener {
            StyleableToast.makeText(requireActivity(), "알림을 이용하시려면 권한 설정이 필요합니다.", R.style.toast_common)
                .show()
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
