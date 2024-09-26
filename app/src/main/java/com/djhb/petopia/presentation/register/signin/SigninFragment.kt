package com.djhb.petopia.presentation.register.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.djhb.petopia.R
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.databinding.FragmentSigninBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.Authority
import com.djhb.petopia.presentation.register.RegisterViewModel
import com.djhb.petopia.presentation.register.signup.SignupFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class SigninFragment : Fragment() {

    private val _binding: FragmentSigninBinding by lazy {
        FragmentSigninBinding.inflate(layoutInflater)
    }
    private val binding get() = _binding

    private val signRepository = SignRepositoryImpl()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //    }
    private val registerViewModel: RegisterViewModel by activityViewModels()

    private var isClickedBack = false
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(isClickedBack) {
                requireActivity().finish()
            } else {
                isClickedBack = true
                Toast.makeText(requireActivity(), "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(2000)
                    isClickedBack = false
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 자동로그인
        val sharedPref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val savedId = sharedPref.getString("saved_id", null)
        val savedPassword = sharedPref.getString("saved_password", null)

        if (savedId != null && savedPassword != null) {
            registerViewModel.signing(
                savedId, savedPassword,
                onSuccess = {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },
                onFailure = {
                    StyleableToast.makeText(
                        requireActivity(),
                        "아이디 또는 비밀번호를 확인해주세요.",
                        R.style.toast_warning
                    ).show()
                }
            )
        }
        // google 로그인

        auth = FirebaseAuth.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("176593614331-5sh34isikt90vbk8f11qcfgltkg18ii3.apps.googleusercontent.com")
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)



        binding.btnSignin.setOnClickListener {
            //로그인버튼 누를시 로그인
            val userInputId = binding.etSigninId.text.toString()
            val userInputPassword = binding.etSigninPassword.text.toString()

            registerViewModel.signing(userInputId, userInputPassword,
                onSuccess = {
                    //로그인 성공
                    saveLoginMethod(false)

                    StyleableToast.makeText(
                        requireActivity(),
                        "로그인 완료",
                        R.style.toast_common
                    ).show()

                    if (binding.cbAutoSignin.isChecked) saveSignInData(
                        userInputId,
                        userInputPassword
                    )

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                },
                onFailure = {
                    //로그인 실패
                    StyleableToast.makeText(
                        requireActivity(),
                        "아이디 또는 비밀번호를 확인해주세요.",
                        R.style.toast_warning
                    ).show()
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

        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)

        //미구현 버튼들

        binding.btnIdSearch.setOnClickListener {
            setFindIdFragment()
        }

        binding.btnPasswordSearch.setOnClickListener {
            showUndoToast()
        }


        binding.btnGoogleSignin.setOnClickListener {
            signInWithGoogle()
        }

    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                firebaseAuthWithGoogle(it.idToken!!)
            }
        } catch (e: ApiException) {
            Log.e("SigninFragment", "Google 로그인 실패", e)
            StyleableToast.makeText(
                requireActivity(),
                "Google 로그인 실패: ${e.localizedMessage}",
                R.style.toast_warning
            ).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let {
                        Log.d("SigninFragment", "Firebase Auth 성공: ${it.uid}")

                        // 기존 아이디가 있을경우 그냥 로그인 아니면 생성 후 로그인
                        registerViewModel.isIdExist(it.uid) { isExist ->
                            if (isExist) {
                                registerViewModel.googleSignIn(it.uid) {
                                    saveLoginMethod(true)
                                    lifecycleScope.launch {
                                        Log.d("Logindata", "${signRepository.selectUser(it.uid)}")
                                    }
                                    val intent =
                                        Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()

                                }
                            } else {
                                val userModel = UserModel(
                                    id = it.uid,
                                    email = it.email ?: "",
                                    nickname = it.displayName ?: "사용자",  // 닉네임이 없는 경우 기본값
                                    authority = Authority.CLIENT  // 기본 권한 설정
                                )
                                lifecycleScope.launch {
                                    try {
                                        signRepository.createUser(userModel)
                                        LoginData.loginUser = userModel
                                        saveLoginMethod(true)
                                        Log.d("LogindataCheck", "$userModel")

                                        val intent =
                                            Intent(requireContext(), MainActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    } catch (e: Exception) {
                                        Log.e("SigninFragment", "Firebase Auth 실패", e)
                                    }

                                }

                            }
                        }
                        StyleableToast.makeText(
                            requireActivity(),
                            "Google 로그인 성공",
                            R.style.toast_common
                        ).show()


                    }
                } else {
                    Log.e("SigninFragment", "Firebase Auth 실패", task.exception)
                    StyleableToast.makeText(
                        requireActivity(),
                        "Google 로그인 실패",
                        R.style.toast_warning
                    ).show()
                }
            }
    }

    fun saveSignInData(id: String, password: String) {
        val sharedPref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("saved_id", id)
            putString("saved_password", password)
            apply()
        }
    }

    private fun setFindIdFragment() {
        FindIdFragment().show(childFragmentManager, "FIND_ID_DIALOG")
    }

    private fun saveLoginMethod(isGoogleLogin: Boolean) {
        val sharedPref =
            requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isGoogleLogin", isGoogleLogin)
            apply()
        }
    }

    fun showUndoToast() {
        StyleableToast.makeText(
            requireActivity(),
            getString(R.string.messege_undo),
            R.style.toast_undo
        ).show()
    }


}