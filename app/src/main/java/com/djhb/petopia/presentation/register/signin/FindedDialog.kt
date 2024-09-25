package com.djhb.petopia.presentation.register.signin

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
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.databinding.FragmentFindedDialogBinding
import com.djhb.petopia.presentation.register.RegisterViewModel

class FindedDialog() : DialogFragment() {

    private val _binding: FragmentFindedDialogBinding by lazy {
        FragmentFindedDialogBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.isIdExist.observe(viewLifecycleOwner) {
            if (it == true) {
                Log.d("FindedDialog", "아이디 존재")
                binding.tvFindedResult.setText("아이디는 ${registerViewModel.userId.value} 입니다.")

            } else {
                binding.tvFindedResult.setText("아이디가 존재하지 않습니다.")
            }
        }

        binding.btnFindedCheck.setOnClickListener {
            dismiss()
        }

    }


    private fun initDialog() {
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        size.x
        size.y
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.5).toInt()
        params?.height = (deviceWidth * 0.5).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}