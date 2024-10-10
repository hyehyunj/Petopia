package com.djhb.petopia.presentation.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.djhb.petopia.data.LoginData
import com.djhb.petopia.data.UserModel
import com.djhb.petopia.data.remote.SignRepository
import com.djhb.petopia.data.remote.SignRepositoryImpl
import com.djhb.petopia.presentation.home.MainHomeGuideSharedViewModel
import kotlinx.coroutines.launch

class RegisterViewModel :
    ViewModel() {


    private val _userNickName = MutableLiveData<String>()
    val userNickName: LiveData<String> = _userNickName

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _userPassword = MutableLiveData<String>()
    val userPassword: LiveData<String> = _userPassword

    private val _userPasswordCheck = MutableLiveData<String>()
    val userPasswordCheck: LiveData<String> = _userPasswordCheck

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    private val _isPasswordMatch = MutableLiveData<Boolean>()
    val isPasswordMatch: LiveData<Boolean> = _isPasswordMatch

    private val _loginUser = MutableLiveData<UserModel?>()
    val loginUser: LiveData<UserModel?> = _loginUser

    private val _userLiveData = MutableLiveData<UserModel?>()
    val userLiveData: LiveData<UserModel?> = _userLiveData

    private val _isIdExist = MutableLiveData<Boolean>()
    val isIdExist: LiveData<Boolean> = _isIdExist

    private val _findedId = MutableLiveData<String>()
    val findedId: LiveData<String> = _findedId

    private val signRepository: SignRepository by lazy {
        SignRepositoryImpl()
    }

    fun modifyNickname(nickname: String) {
        _userNickName.value = nickname

        LoginData.loginUser.let { user ->
            user.nickname = nickname
            updateUser()
        }
    }

    fun modifyPassword(password: String) {
        _userPassword.value = password

        LoginData.loginUser.let { user ->
            user.password = password
            updateUser()
        }
    }

    fun setUserData(
        userNickname: String,
        userId: String,
        userPassword: String,
        userPasswordCheck: String,
        userEmail: String
    ) {
        _userNickName.value = userNickname
        _userId.value = userId
        _userPassword.value = userPassword
        _userPasswordCheck.value = userPasswordCheck
        _userEmail.value = userEmail
    }

    fun signing(
        inputId: String,
        inputPassword: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            val user = signRepository.selectUser(inputId)
            if (user.id.isNotEmpty() && user.password == inputPassword) {
                LoginData.loginUser = user
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun googleSignIn(
        inputId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val user = signRepository.selectUser(inputId)
            if (user.id.isNotEmpty()) {
                LoginData.loginUser = user
                onSuccess()
            }
        }
    }

    fun checkIdExist(
        inputNickname: String,
        inputEmail: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        viewModelScope.launch {
            val user = signRepository.selectNickname(inputNickname)

            if (user.nickname.isNotEmpty() && user.email == inputEmail) {
                _userId.value = user.id
                //닉네임으로 찾은 아이디 뷰모델에 저장
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun setIsIdExist(isExist: Boolean) {
        _isIdExist.value = isExist
    }

    fun setFindedId(id: String) {
        _findedId.value = id
    }


//    fun checkPasswordMatch(
//        inputPassword: String,
//        onSuccess: () -> Unit,
//        onFailure: () -> Unit
//    ) {
//        viewModelScope.launch {
//            val user = signRepository.selectUser(inputPassword)
//            if(user != null && user.password == inputPassword){
//                onSuccess()
//            }else{
//                onFailure()
//            }
//        }
//    }

    fun isIdExist(
        id: String, onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = signRepository.selectUser(id)
            onResult(user.id.isNotEmpty())
        }
    }

    fun createUser(user: UserModel) {
        viewModelScope.launch {
            signRepository.createUser(user)
        }
    }

    fun deleteUser(
        id: String,
    ) {
        viewModelScope.launch {
            signRepository.deleteUser(id)
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            signRepository.updateUser(LoginData.loginUser)
        }
    }

    fun saveUser(user: UserModel) {
        _userLiveData.value = user
    }


//    class RegisterViewModelFactory(
//        private val mainHomeGuideSharedViewModel: MainHomeGuideSharedViewModel
//    ) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return RegisterViewModel(mainHomeGuideSharedViewModel) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
}