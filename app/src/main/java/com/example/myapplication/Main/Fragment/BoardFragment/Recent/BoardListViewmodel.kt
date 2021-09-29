package com.example.myapplication.Main.Fragment.BoardFragment.Recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo

class BoardListViewmodel : ViewModel() {
    private var repo = Repo.StaticFunction.getInstance()
    private lateinit var listdata : MutableList<BoardDTO>
    private lateinit var contentsUid : ArrayList<String>
    private var count = 1

    fun getboarddata(): MutableList<BoardDTO>{
        return repo.getboarddata()
    }
    fun getboardUid(): ArrayList<String>{
        return repo.getboardUid()
    }
    fun getListdata(): LiveData<BoardDTO>{
        return repo.getListdata()
    }
    fun getlistuid(): LiveData<String>{
        return repo.getlistuid()
    }
}