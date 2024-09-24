package com.djhb.petopia.presentation.my

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
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentMyBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.register.RegisterActivity
import com.djhb.petopia.presentation.register.RegisterViewModel


class MyFragment : DialogFragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding as FragmentMyBinding

    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)

        binding.myTvName.text = LoginData.loginUser.nickname

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myButtonClickListener()
        modifyButtonClickListener()
        modifyPasswordButtonClickListener()
        deleteButtonClickListener()

        registerViewModel.userNickName.observe(viewLifecycleOwner) { user ->
            binding.myTvName.text = user
        }

        backButtonClickListener()
    }


    private fun myButtonClickListener() {

        binding.myTvPetEdit.setOnClickListener {
            MyPetEditFragment().show(childFragmentManager, "MY_PET_EDIT_FRAGMENT")
        }
        binding.myTvPetopiaIntro.setOnClickListener {
            (activity as MainActivity).showIntroFragment()
        }
        binding.myTvGuide.setOnClickListener {
            (activity as MainActivity).welcomeGuide()
        }

        binding.myBtnLogout.setOnClickListener {
            logout()
        }


    }

    private fun modifyButtonClickListener() {
        binding.myTvEdit.setOnClickListener {
            modify()
        }
    }

    private fun modifyPasswordButtonClickListener() {
        binding.myTvPasswordEdit.setOnClickListener {
            modifyPassword()
        }
    }



    private fun backButtonClickListener() {
        binding.myIvBack.setOnClickListener {
            dismiss()
        }
    }

    private fun deleteButtonClickListener() {
        binding.myTvWithdraw.setOnClickListener {
            deleteUser()
        }
    }



    private fun logout() {
        //자동로그인 해제
        val sharedPref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        //다이얼로그를 닫고 이동
        dismiss()
        //RegisterActivity 재실행
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    //닉네임 수정페이지
    private fun modify() {
        UserModifyFragment().show(childFragmentManager, "MODIFY_DIALOG")
    }

    //비밀번호 수정페이지
    private fun modifyPassword() {
        UserPasswordModifyFragment().show(childFragmentManager, "MODIFY_DIALOG")
    }

    private fun deleteUser() {
        DeleteUserFragment().show(childFragmentManager, "DELETE_DIALOG")
    }


    override fun onResume() {
        super.onResume()
    }
}