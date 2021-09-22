package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.KeyboardVisibilityUtils
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_comment.*
import kotlinx.android.synthetic.main.activity_board_comment.sv_root
import java.text.SimpleDateFormat
import java.util.*

class BoardChat : AppCompatActivity(){
    private var firestore =FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Chats = BoardDTO.Chat()
    private var lastMessage = MessageDTO.lastMessage()
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils //키보드 움직이기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_comment)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = { keyboardHeight ->
                sv_root.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })  //키보드 움직이기

        var context = this
        // 해당 게시글 uid를 intent로 받아옴
        val commentUid = intent.getStringExtra("commentUid")!!
        firestore.collection("userid").document(uid!!).get().addOnCompleteListener {
            if(it.isSuccessful){
                currentDTO = it.result?.toObject(UserinfoDTO::class.java)!!
            }
        }
        comment_recyclerView.apply {
            var chatAdapter : ChatAdapter
            layoutManager = LinearLayoutManager(context)
            chatAdapter = ChatAdapter(commentUid)
            adapter = chatAdapter
        }
        btn_comment_send?.setOnClickListener{
            // 채팅내용 업로드
            Chats.UID = uid
            Chats.message = comment_text.text.toString()
            Chats.userprofile = currentDTO.ProfileUrl
            Chats.userNickname = currentDTO.nickname
            Chats.timestamp = System.currentTimeMillis()
            firestore.collection("Chat").document(commentUid).collection("Messages").document().set(Chats)
            setLastMessage()
            comment_text.setText("")
        }
    }
    private fun setLastMessage(){
        var time = SimpleDateFormat("MM월 dd일").format(Date())
        var lastchat = comment_text.text.toString()
        var commentUid = intent.getStringExtra("commentUid")!!
        var docName = commentUid + "_last"

        var DoR = firestore.collection("Chat").document(commentUid)
        DoR.collection("LastMessage").document(docName).update(
            mapOf(
                "senderuid" to uid,
                "lastContent" to lastchat,
                "time" to time,
                "profileUrl" to currentDTO.ProfileUrl,
                "nickname" to currentDTO.nickname
            )
        )
        setResult(RESULT_OK)
    }


}