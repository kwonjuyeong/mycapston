package com.example.myapplication.let

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Main.Activity.MainActivity.Companion.TAG
import com.example.myapplication.let.BaseViewModel
import com.example.myapplication.let.LoginReq
import com.example.myapplication.let.SignInRepository
import com.example.myapplication.let.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: SignInRepository) :
    BaseViewModel() {

    var key: String = ""
    var email = MutableLiveData("")
    var pw = MutableLiveData("")

    private val _goSignUp = SingleLiveEvent<Unit>()
    private val _loginResult = MutableLiveData<Boolean>()

    val goSignUp: LiveData<Unit> get() = _goSignUp
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun goSignUp() {
        _goSignUp.call()
    }

    fun requestLogin() {
        repository.requestLogin(
            LoginReq(email.value!!, pw.value!!)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate { hideProgress() }
            .subscribeBy(onSuccess = {
                Log.d(TAG, "requestLogin() KEY -> " + it.key)
                key = it.key
                _loginResult.value = true
            }, onError = {
                Log.d(TAG, "requestLogin() -> " + it.localizedMessage)
                _loginResult.value = false
            }).addTo(compositeDisposable)
    }
}