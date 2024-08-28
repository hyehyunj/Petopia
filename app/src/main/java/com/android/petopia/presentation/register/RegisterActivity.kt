package com.android.petopia.presentation.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petopia.databinding.ActivitySigninBinding

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}