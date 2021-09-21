package com.example.myapplication.Main.Board.Detail.Comment

import com.example.myapplication.DTO.BoardDTO

interface ChatListener {
    fun getComment(listener: ChatListener, comment_info : MutableList<BoardDTO.Chat>, commentUid : String)
    fun loadView()
}