/*package com.example.myapplication.let

import androidx.lifecycle.LiveData
import com.example.myapplication.let.BaseViewModel
import com.example.myapplication.let.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpCompleteViewModel @Inject constructor() : BaseViewModel() {

    private val _goLogin = SingleLiveEvent<Unit>()

    val goLogin: LiveData<Unit> get() = _goLogin

    fun goLogin() {
        _goLogin.call()
    }
}*/