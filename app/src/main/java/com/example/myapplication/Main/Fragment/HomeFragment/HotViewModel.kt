package com.example.myapplication.Main.Fragment.HomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.Util

class HotViewModel : ViewModel(){
    private var util = Util.StaticFunction.getInstance()

    fun getHotListData() : LiveData<MutableList<BoardDTO>>{
        val hotListData = MutableLiveData<MutableList<BoardDTO>>()
        util.getAroundHot().observeForever {
            hotListData.value = it
        }
        return hotListData
    }
    fun getUidlistData(): LiveData<MutableList<String>>{
        val hotListId = MutableLiveData<MutableList<String>>()
        util.getAroundId().observeForever {
            hotListId.value = it
        }
        return hotListId
    }
}