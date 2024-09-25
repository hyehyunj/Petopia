package com.djhb.petopia.presentation.register.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.R
import com.djhb.petopia.databinding.FragmentFindIdBinding
import com.djhb.petopia.presentation.register.RegisterViewModel
import io.github.muddz.styleabletoast.StyleableToast


class FindIdFragment : DialogFragment() {

    private val _binding: FragmentFindIdBinding by lazy {
        FragmentFindIdBinding.inflate(layoutInflater)
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

        binding.btnFindIdCheck.setOnClickListener {
            val userNick = binding.etExistUsernick.text.toString()
            val userEmail = binding.etFindIdPassword.text.toString()

            if (userNick.isEmpty() || userEmail.isEmpty()) {
                StyleableToast.makeText(
                    requireActivity(),
                    "닉네임 또는 이메일을 확인해주세요.",
                    R.style.toast_warning
                ).show()
            } else {

                registerViewModel.checkIdExist(userNick, userEmail,
                    onSuccess = {
                        registerViewModel.setIsIdExist(true)
                        registerViewModel.setFindedId(registerViewModel.userId.value.toString())
                        showFindedDialog()
                    },
                    onFailure = {
                        registerViewModel.setIsIdExist(false)
                        showFindedDialog()
                    }
                )
            }
        }

        binding.btnFindIdCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun showFindedDialog() {
        FindedDialog().show(childFragmentManager, "FINDE_DIALOG")
    }


}


