package com.djhb.petopia.presentation.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.databinding.FragmentMyBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.register.RegisterActivity
import com.djhb.petopia.presentation.register.RegisterViewModel
import com.djhb.petopia.presentation.register.signup.TermFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MyFragment : DialogFragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding as FragmentMyBinding

    private val registerViewModel: RegisterViewModel by activityViewModels()


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
        termButtonClickListener()

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
            MyGuideDialogFragment().show(childFragmentManager, "MY_GUIDE_FRAGMENT")
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

    private fun termButtonClickListener() {
        binding.myTvTerms.setOnClickListener {
            toTermFragment()
        }
    }


    private fun logout() {
        val sharedPref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val isGoogleLogin = sharedPref.getBoolean("isGoogleLogin", false)

        if (isGoogleLogin) {
            googleLogout()
        } else {
            normalLogout()
        }
        //다이얼로그를 닫고 이동
        dismiss()
        //RegisterActivity 재실행
        val intent = Intent(requireContext(), RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun googleLogout() {
        val googleSignInClient =
            GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                val sharedPref =
                    requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("isGoogleLogin", false)
                    apply()
                }
            }
    }

    private fun normalLogout() {
        val sharedPref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
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

    private fun toTermFragment() {
        TermFragment().show(childFragmentManager, "TERM_DIALOG")
    }

    override fun onResume() {
        super.onResume()
    }
}