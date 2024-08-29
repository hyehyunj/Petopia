package com.android.petopia.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

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

    fun validateSignin(inputId: String, inputPassword: String): Boolean {
        val isValidId = inputId == _userId.value && inputPassword == _userPassword.value
        _isPasswordMatch.value = isValidId
        return isValidId
    }

}