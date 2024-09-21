package com.djhb.petopia.presentation.my

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentUserPasswordModifyBinding
import com.djhb.petopia.presentation.register.RegisterViewModel
import io.github.muddz.styleabletoast.StyleableToast


class UserPasswordModifyFragment : DialogFragment() {

    private val _binding: FragmentUserPasswordModifyBinding by lazy {
        FragmentUserPasswordModifyBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()


        binding.btnModifyPasswordCheck.setOnClickListener {
            modifyPassword()
        }

        binding.btnModifyPasswordCancel.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .remove(this@UserPasswordModifyFragment).commit()
                }
            })
    }

    private fun modifyPassword() {
        val existPassword = binding.etExistPassword.text.toString()
        val modifyPassword = binding.etModifyPassword.text.toString()
        val passwordCheck = binding.etModifyPasswordCheck.text.toString()

        if (existPassword.isEmpty() || modifyPassword.isEmpty() || passwordCheck.isEmpty()) {
            StyleableToast.makeText(
                requireActivity(),
                "모든 항목을 입력해주세요.",
                R.style.toast_warning
            ).show()
        }


        if (existPassword != LoginData.loginUser.password) {

            Log.d("existPassword", existPassword)
            Log.d("userPassword", LoginData.loginUser.password)

            StyleableToast.makeText(
                requireActivity(),
                "기존 비밀번호가 일치하지 않습니다.",
                R.style.toast_warning
            ).show()
        } else if (modifyPassword == LoginData.loginUser.password) {
            StyleableToast.makeText(
                requireActivity(),
                "기존 비밀번호로 변경할 수 없습니다.",
                R.style.toast_warning
            ).show()
        }  else if(modifyPassword != passwordCheck) {
            StyleableToast.makeText(
                requireActivity(),
                "비밀번호가 일치하지 않습니다.",
                R.style.toast_warning
            ).show()
        } else{
            registerViewModel.modifyPassword(modifyPassword)
            StyleableToast.makeText(
                requireActivity(),
                "비밀번호 변경완료",
                R.style.toast_warning
            ).show()

            parentFragmentManager.beginTransaction().remove(this).commit()
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
        params?.width = (deviceWidth * 1).toInt()
        params?.height = (deviceWidth * 2.05).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}