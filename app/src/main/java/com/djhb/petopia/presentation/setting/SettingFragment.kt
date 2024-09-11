package com.djhb.petopia.presentation.setting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentSettingBinding
import com.djhb.petopia.presentation.register.RegisterActivity
import com.djhb.petopia.presentation.register.signin.SigninFragment
import com.djhb.petopia.presentation.register.signup.SignupFragment


class SettingFragment : DialogFragment() {

    private val _binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()

        binding.apply {
            btnSettingExit.setOnClickListener {
                dismiss()
            }
            btnSettingLogout.setOnClickListener {
                loguout()
            }
            btnSettingDeleteAccount.setOnClickListener {
                dismiss()
            }
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
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceWidth * 1.8).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun loguout() {
        clearSharedPreferences()
        //다이얼로그를 닫고 이동
        dismiss()

        //RegisterActivity 재실행
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun clearSharedPreferences() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MemoryisSaved", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}