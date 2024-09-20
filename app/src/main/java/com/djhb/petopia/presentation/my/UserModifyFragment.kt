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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.databinding.FragmentUserModifyBinding
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import com.djhb.petopia.presentation.register.RegisterViewModel


class UserModifyFragment : DialogFragment() {

    private val _binding: FragmentUserModifyBinding by lazy {
        FragmentUserModifyBinding.inflate(
            layoutInflater
        )
    }
    private val binding get() = _binding

    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val mainHomeGuideSharedViewModel: MainHomeGuideSharedViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()

        binding.btnSignupCheck.setOnClickListener {
            if (binding.etModifyNickname.text.toString()
                    .isNotEmpty() && binding.etModifyNickname.text.toString().length <= 6
            ) {
                registerViewModel.modifyNickname(binding.etModifyNickname.text.toString())
                parentFragmentManager.beginTransaction().remove(this).commit()

            }

        }

        binding.btnSignupCancel.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.beginTransaction()
                        .remove(this@UserModifyFragment).commit()
                }
            })
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