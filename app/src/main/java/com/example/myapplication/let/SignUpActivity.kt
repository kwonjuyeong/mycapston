/*package com.example.myapplication.let.com.example.myapplication.let

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.let.BaseActivity
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.example.myapplication.let.PhoneAuthActivity
import com.example.myapplication.let.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_add_login) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            goPhoneAuth.observe(this@SignUpActivity, Observer {
                startActivity(Intent(this@SignUpActivity, PhoneAuthActivity::class.java))
            })

        }
    }
}*/