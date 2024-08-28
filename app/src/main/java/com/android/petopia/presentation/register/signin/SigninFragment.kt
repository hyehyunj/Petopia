package com.android.petopia.presentation.register.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petopia.R
import com.android.petopia.databinding.FragmentSigninBinding
import com.android.petopia.presentation.register.signup.SignupFragment


class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            //로그인버튼 누를시 로그인
        }

        //회원가입 누르면 회원가입 프래그먼트로 이동
        binding.btnSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.register_fragment_container, SignupFragment())
                .addToBackStack(null)
                .commit()
        }

    }


}