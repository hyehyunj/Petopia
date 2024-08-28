package com.android.petopia.presentation.register.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.R
import com.android.petopia.databinding.FragmentSignupBinding
import com.android.petopia.presentation.register.signin.SigninFragment


class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignupCheck.setOnClickListener {
            // 회원가입 완료 ->
            // 1. 비밀번호, 비밀번호확인 일치여부 확인
            // 2. 회원가입정보 서버에 전달
            // 3. 로그인페이지로 이동

        }

        binding.btnSignupCancel.setOnClickListener {
            //회원가입 취소 -> 로그인페이지로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.register_fragment_container, SigninFragment())
                .addToBackStack(null)
                .commit()

        }

    }

}