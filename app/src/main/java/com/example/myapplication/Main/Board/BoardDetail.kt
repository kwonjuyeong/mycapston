package com.example.myapplication.Main.Board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class BoardDetail : AppCompatActivity() {
    private var firestore : FirebaseFirestore? = null
    private var boarddto = BoardDTO()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

    }

}