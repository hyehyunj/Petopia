package com.djhb.petopia.presentation.my

import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentDeleteUserBinding
import com.djhb.petopia.databinding.FragmentUserPasswordModifyBinding
import com.djhb.petopia.presentation.register.RegisterActivity
import com.djhb.petopia.presentation.register.RegisterViewModel
import io.github.muddz.styleabletoast.StyleableToast

class DeleteUserFragment : DialogFragment() {

    private val _binding: FragmentDeleteUserBinding by lazy {
        FragmentDeleteUserBinding.inflate(layoutInflater)
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

        Log.d("loginuser", LoginData.loginUser.id)

        binding.btnUserDeleteCheck.setOnClickListener {
            if (binding.cbUserDeleteLastCheck.isChecked) {
                Log.d("checkbox", binding.cbUserDeleteLastCheck.isChecked.toString())
                userDelete()

            } else {
                StyleableToast.makeText(
                    requireActivity(),
                    "회원탈퇴를 진행하시려면 동의버튼을 체크해주세요.",
                    R.style.toast_warning
                ).show()
            }
        }




        binding.btnUserDeleteCancel.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    private fun userDelete() {
        registerViewModel.deleteUser(LoginData.loginUser.id)


        registerViewModel.updateUser()

        dismiss()
        //RegisterActivity 재실행
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
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

}