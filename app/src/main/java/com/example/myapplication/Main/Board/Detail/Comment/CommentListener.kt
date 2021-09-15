package com.example.myapplication.Main.Board.Detail.Comment

import com.example.myapplication.DTO.BoardDTO

interface CommentListener {
    fun getComment(listener: CommentListener, comment_info : MutableList<BoardDTO.Comment>, commentUid : String)
    fun loadView()
}