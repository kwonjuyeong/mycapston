/*package com.example.myapplication.let

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.let.BaseActivity
import com.example.myapplication.databinding.ActivitySignUpCompleteBinding
import com.example.myapplication.let.SignInActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpCompleteActivity :
    BaseActivity<ActivitySignUpCompleteBinding>(R.layout.activity_main) {
    private val viewModel: SignUpCompleteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initViewModelCallback()
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            goLogin.observe(this@SignUpCompleteActivity, Observer {
                val intent = Intent(this@SignUpCompleteActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            })
        }
    }
}*/