package com.example.myapplication.let

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.let.BaseActivity
import com.example.myapplication.databinding.ActivityUserInfoBinding
import com.example.myapplication.let.UserInfo
//import com.example.myapplication.let.sign_in.SignInActivity
import com.example.myapplication.Main.Activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>(R.layout.activity_user_info) {
    private val viewModel: UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        initViewModelCallback()
        initView()
    }

    private fun initView() {
        binding.userInfoEtUserPhone.setText(UserInfo.tel)
        viewModel.userTel.value = UserInfo.tel
        // binding.userInfoEtUserPhone.isEnabled = false
    }

    private fun initViewModelCallback() {
        with(viewModel) {
            completeFrom.observe(this@UserInfoActivity, Observer {
                binding.run {
                    when {
                        businessName.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtBusinessName.requestFocus()
                        }
                        businessCode.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtBusinessCode.requestFocus()
                        }
                        userName.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtUserName.requestFocus()
                        }
                        userRank.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtUserRank.requestFocus()
                        }
                        userEmail.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtUserEmail.requestFocus()
                        }
                        userPw.value!!.isBlank() -> {
                            showToast("정보를 모두 작성해주세요")
                            userInfoEtUserPw.requestFocus()
                        }
                        else -> {
                            insertUserInfo()
                        }
                    }
                }
            })

            insertUserInfoResult.observe(this@UserInfoActivity, Observer { success ->
                if (success) {
                    val intent = Intent(this@UserInfoActivity, com.example.myapplication.Main.Activity.MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            })


        }
    }
}