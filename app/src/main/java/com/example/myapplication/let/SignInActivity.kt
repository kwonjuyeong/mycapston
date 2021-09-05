/*package com.example.myapplication.let

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.let.BaseActivity
import com.example.myapplication.databinding.ActivitySignInBinding
import com.example.myapplication.let.UserInfo
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.let.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_login) {
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            loginResult.observe(this@SignInActivity, Observer {
                if (it) {
                    UserInfo.headerKey = viewModel.key
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
                else showToast("로그인 실패")
            })

            goSignUp.observe(this@SignInActivity, Observer {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            })
        }
    }
}*/