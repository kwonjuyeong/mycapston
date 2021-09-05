package com.example.myapplication.let

import com.example.myapplication.let.ApiInterface
import com.example.myapplication.let.LoginReq
import com.example.myapplication.let.LoginRes
import io.reactivex.Single
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface) :
    SignInRepository {
    override fun requestLogin(loginReq: LoginReq): Single<LoginRes> {
        return apiInterface.requestLogin(loginReq.email, loginReq.password)
    }
}

interface SignInRepository {
    fun requestLogin(loginReq: LoginReq): Single<LoginRes>
}
