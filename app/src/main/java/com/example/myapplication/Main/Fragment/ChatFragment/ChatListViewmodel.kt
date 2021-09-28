package com.example.myapplication.Main.Fragment.ChatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DTO.MessageDTO

class ChatListViewModel : ViewModel() {
    fun getChatListData() : LiveData<MessageDTO.lastMessage> {
        return ChatRepo.getLatestData()
    }
}