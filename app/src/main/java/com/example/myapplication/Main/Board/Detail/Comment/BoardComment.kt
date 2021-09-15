package com.example.myapplication.Main.Board.Detail.Comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_commant.*

class BoardComment : AppCompatActivity(), CommentListener{
    private var Commentdto = BoardDTO.Comment()
    private var firestore =FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Comment = BoardDTO.Comment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_commant)
        var context = this
        // 해당 게시글 uid를 intent로 받아옴
        val commentUid = intent.getStringExtra("commentUid")!!
        firestore.collection("userid").document(uid!!).get().addOnCompleteListener {
            if(it.isSuccessful){
                currentDTO = it.result?.toObject(UserinfoDTO::class.java)!!
            }
        }
        comment_recyclerView.apply {
            var commentAdapter : CommentAdapter
            layoutManager = LinearLayoutManager(context)
            commentAdapter = CommentAdapter(commentUid)
            adapter = commentAdapter
        }
        btn_comment_send?.setOnClickListener{
            Comment.UID = uid
            Comment.comment = comment_text.text.toString()
            Comment.userprofile = currentDTO.ProfileUrl
            Comment.userNickname = currentDTO.nickname
            Comment.timestamp = System.currentTimeMillis()

            FirebaseFirestore.getInstance().collection("Board").document(commentUid).collection("Comments").document().set(Comment)
            comment_text.setText("")
        }
    }

    override fun loadView() {

    }

    override fun getComment(listener: CommentListener,comment_info : MutableList<BoardDTO.Comment>, commentUid : String) {
        val callback = listener
        FirebaseFirestore.getInstance().collection("Board").document(commentUid).collection("Comments").document()

        callback.loadView()
    }
}