package com.android.petopia.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.petopia.data.LoginData
import com.android.petopia.data.UserModel
import com.android.petopia.data.remote.SignRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val signRepository: SignRepository) : ViewModel() {

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

    fun createUser(user: UserModel) {
        viewModelScope.launch {
            signRepository.createUser(user)
        }
    }

    class RegisterViewModelFactory(
        private val signRepository: SignRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(signRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}