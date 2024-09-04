package com.djhb.petopia.presentation.register.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.R
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.databinding.FragmentSigninBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.register.RegisterViewModel
import com.djhb.petopia.presentation.register.signup.SignupFragment


class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    private val signRepository = SignRepositoryImpl()
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModel.RegisterViewModelFactory(signRepository)
    }


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
            val userInputId = binding.etSigninId.text.toString()
            val userInputPassword = binding.etSigninPassword.text.toString()

            registerViewModel.signing(userInputId, userInputPassword,
                onSuccess = {
                    //로그인 성공
                    Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },
                onFailure = {
                    //로그인 실패
                    Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            )
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