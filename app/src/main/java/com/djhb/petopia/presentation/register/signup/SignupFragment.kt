package com.djhb.petopia.presentation.register.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.databinding.FragmentSignupBinding
import com.djhb.petopia.presentation.register.RegisterViewModel
import com.djhb.petopia.presentation.register.signin.SigninFragment
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.launch


class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModel.RegisterViewModelFactory(SignRepositoryImpl())
    }

    private lateinit var userNickname: String
    private lateinit var userId: String
    private lateinit var userPassword: String
    private lateinit var userPasswordCheck: String
    private lateinit var userEmail: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //약관 더보기 버튼
        binding.btnViewmore.setOnClickListener {
            binding.cbCheckTerms.visibility = View.VISIBLE
            binding.btnReadTerms.visibility = View.VISIBLE
            binding.cbPersonalInfoTerm.visibility = View.VISIBLE
            binding.btnReadTerms2.visibility = View.VISIBLE
        }

        //약관 체크 관련
        binding.cbCheckTerms.setOnClickListener {
            if (binding.cbCheckTerms.isChecked && binding.cbPersonalInfoTerm.isChecked) {
                binding.cbCheckAgreeAll.isChecked = true
            } else if (!binding.cbCheckTerms.isChecked || !binding.cbPersonalInfoTerm.isChecked) {
                binding.cbCheckAgreeAll.isChecked = false
            }
        }
        binding.cbPersonalInfoTerm.setOnClickListener {
            if (binding.cbCheckTerms.isChecked && binding.cbPersonalInfoTerm.isChecked) {
                binding.cbCheckAgreeAll.isChecked = true
            } else if (!binding.cbCheckTerms.isChecked || !binding.cbPersonalInfoTerm.isChecked) {
                binding.cbCheckAgreeAll.isChecked = false
            }
        }

        binding.cbCheckAgreeAll.setOnClickListener {
            if (binding.cbCheckAgreeAll.isChecked) {
                binding.cbCheckTerms.isChecked = true
                binding.cbPersonalInfoTerm.isChecked = true
            } else {
                binding.cbCheckTerms.isChecked = false
                binding.cbPersonalInfoTerm.isChecked = false
            }
        }

        binding.btnReadTerms.setOnClickListener {
            setTermFragment()
        }

        binding.btnReadTerms2.setOnClickListener {
            setPersonalTermFragment()
        }

//        if (binding.cbCheckAgreeAll.isChecked) {
//            binding.btnSignupCheck.isEnabled = true
//        } else {
//            binding.btnSignupCheck.isEnabled = false
//        }

        binding.btnSignupCheck.setOnClickListener {
            // 회원가입 완료 ->
            // 1. 비밀번호, 비밀번호확인 일치여부 확인
            // 2. 회원가입정보 서버에 전달
            // 3. 로그인페이지로 이동
            if (isfilled()) {
                usersignindata()
            } else {
                Log.d("회원가입", "회원가입 실패")
            }


        }

        binding.btnSignupCancel.setOnClickListener {
            //회원가입 취소 -> 로그인페이지로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.register_fragment_container, SigninFragment())
                .addToBackStack(null)
                .commit()

        }

    }

    private fun isfilled(): Boolean {
        userNickname = binding.etSignupNickname.text.toString()
        userId = binding.etSignupId.text.toString()
        userPassword = binding.etSignupPassword.text.toString()
        userPasswordCheck = binding.etSignupPasswordCheck.text.toString()
        userEmail = binding.etSignupEmail.text.toString()

        val field = mapOf(
            "닉네임" to userNickname,
            "아이디" to userId,
            "비밀번호" to userPassword,
            "비밀번호확인" to userPasswordCheck,
            "이메일" to userEmail
        )

        for ((fieldName, value) in field) {
            if (value.isEmpty()) {
                StyleableToast.makeText(
                    requireActivity(),
                    "${fieldName}을 입력해주세요.",
                    R.style.toast_warning
                ).show()
                return false
            }
        }

        if (userPassword != userPasswordCheck) {
            StyleableToast.makeText(
                requireActivity(),
                "비밀번호가 일치하지 않습니다.",
                R.style.toast_warning
            ).show()
            return false
        }

        if (!binding.cbCheckAgreeAll.isChecked) {
            StyleableToast.makeText(
                requireActivity(),
                "모든 약관에 동의해주세요.",
                R.style.toast_warning
            ).show()
            return false
        }

        return true
    }

    private fun usersignindata() {
        val user = UserModel(
            id = userId,
            password = userPassword,
            name = userNickname,
            email = userEmail
        )

        lifecycleScope.launch {
            registerViewModel.createUser(user)
            StyleableToast.makeText(
                requireActivity(),
                "회원가입 성공",
                R.style.toast_common
            ).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.register_fragment_container, SigninFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setTermFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.register_fragment_container, TermFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setPersonalTermFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.register_fragment_container, PersonalTermFragment())
            .addToBackStack(null)
            .commit()

    }
}
