package com.djhb.petopia.presentation.register.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.djhb.petopia.R
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.databinding.FragmentSigninBinding
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.community.Authority
import com.djhb.petopia.presentation.register.RegisterViewModel
import com.djhb.petopia.presentation.register.signup.SignupFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.muddz.styleabletoast.StyleableToast
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

    //    private val registerViewModel: RegisterViewModel by activityViewModels {
//        RegisterViewModel.RegisterViewModelFactory(signRepository)
//    }
    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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
                    StyleableToast.makeText(
                        requireActivity(),
                        "로그인 완료",
                        R.style.toast_common
                    ).show()

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

        //미구현 버튼들

        binding.btnIdSearch.setOnClickListener {
            showUndoToast()
        }

        binding.btnPasswordSearch.setOnClickListener {
            showUndoToast()
        }

        binding.btnKakaoSignin.setOnClickListener {
            showUndoToast()
        }

        binding.btnGoogleSignin.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnNaverSignin.setOnClickListener {
            showUndoToast()
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

                        val userModel = UserModel(
                            id = it.uid,
                            email = it.email ?: "",
                            nickname = it.displayName ?: "사용자",  // 닉네임이 없는 경우 기본값
                            authority = Authority.CLIENT  // 기본 권한 설정
                        )

                        // ViewModel 또는 로컬/원격 저장소에 UserModel 저장
                        registerViewModel.saveUser(userModel)

                        StyleableToast.makeText(
                            requireActivity(),
                            "Google 로그인 성공",
                            R.style.toast_common
                        ).show()

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
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


    fun showUndoToast() {
        StyleableToast.makeText(
            requireActivity(),
            getString(R.string.messege_undo),
            R.style.toast_undo
        ).show()
    }


}