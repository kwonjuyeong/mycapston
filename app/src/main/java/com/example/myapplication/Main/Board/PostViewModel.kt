package com.example.myapplication.Main.Board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val _selectedMonth = MutableLiveData<String>()
    val selectedMonth: LiveData<String> = _selectedMonth

    fun setSelectedMonth(tag: String) {
        _selectedMonth.value = tag
    }
}