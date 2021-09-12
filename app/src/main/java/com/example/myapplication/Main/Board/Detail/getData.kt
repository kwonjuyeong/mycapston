package com.example.myapplication.Main.Board.Detail

import com.example.myapplication.DTO.BoardDTO
import com.google.firebase.firestore.FirebaseFirestore

class getData(listener: PostListener, Path : String, private val firestore : FirebaseFirestore) {
//    firestore.getInstance()
    var callback = listener
    var boarddto = BoardDTO()

//    firestore?.collection("Board")?.document(Path.toString()!!)?.get()
//    ?.addOnSuccessListener { task ->
//        boarddto = task?.toObject(BoardDTO::class.java)!!
//    }
//    callback.loadPage(boarddto)
}
