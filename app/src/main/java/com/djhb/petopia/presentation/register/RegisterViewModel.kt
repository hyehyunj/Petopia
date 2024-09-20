package com.djhb.petopia.presentation.register

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

    private val signRepository: SignRepository by lazy {
        SignRepositoryImpl()
    }

    fun modifyNickname(nickname: String) {
        _userNickName.value = nickname

        LoginData.loginUser?.let { user ->
            user.nickname = nickname
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
            if (user != null && user.password == inputPassword) {
                LoginData.loginUser = user
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun isIdExist(
        id: String, onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = signRepository.selectUser(id)
            onResult(user != null)
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