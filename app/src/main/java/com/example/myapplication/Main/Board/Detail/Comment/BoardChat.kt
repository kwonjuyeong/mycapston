package com.example.myapplication.Main.Board.Detail.Comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.KeyboardVisibilityUtils
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_comment.*
import kotlinx.android.synthetic.main.activity_board_comment.sv_root

class BoardChat : AppCompatActivity(), ChatListener{
    private var Commentdto = BoardDTO.Chat()
    private var firestore =FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Chats = BoardDTO.Chat()
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
            Chats.OwnerUid = currentDTO.UID
            Chats.UID = uid
            Chats.message = comment_text.text.toString()
            Chats.userprofile = currentDTO.ProfileUrl
            Chats.userNickname = currentDTO.nickname
            Chats.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance().collection("Board").document(commentUid).collection("Chat").document().set(Chats)
            comment_text.setText("")
        }
    }

    override fun loadView() {

    }

    override fun getComment(listener: ChatListener, comment_info : MutableList<BoardDTO.Chat>, commentUid : String) {
        val callback = listener
        FirebaseFirestore.getInstance().collection("Board").document(commentUid).collection("Chat").document()

        callback.loadView()
    }
}