package com.example.myapplication.Main.Board.Detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.Main.Board.Detail.Chat.BoardChat
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_detail.*


class BoardDetail : AppCompatActivity(), PostListener {
    private var boarddto = BoardDTO()
    private var firestore = FirebaseFirestore.getInstance()
    private var messageDTO = MessageDTO()
    private var lastmessage = MessageDTO.lastMessage()
    val chooseUid: String? = null
    var uid : String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        createChatting()
        val chooseUid = intent.getStringExtra("contentsUid")!!
        getData(this, chooseUid)
        BoardCheck_like.setOnClickListener{
            likeupdate()
        }
        //1번
//        BoardCheck_commend.setOnClickListener{
//            intent.putExtra("chooseUid", chooseUid)
//            startActivity(Intent(this,BoardChat::class.java))
//        }
        //2번
        BoardCheck_commend.setOnClickListener{
            joinChat()
            val intent = Intent(this, BoardChat::class.java)
            intent.putExtra("commentUid", chooseUid)
            ContextCompat.startActivity(this, intent,null)

        }
//        if(boarddto.like.containsKey(uid)){
//            // 좋아요 클릭한 경우
//            BoardCheck_like.setImageResource(R.drawable.favorite)
    //        }else{
//            //좋아요 클릭하지 않은 경우
//            BoardCheck_like.setImageResource(R.drawable.favorite_border)
//        }

    }
    private fun createChatting(){
        val chooseUid = intent.getStringExtra("contentsUid")!!
        val owneruid = intent.getStringExtra("owneruid")!!
        messageDTO.boardUid = chooseUid
        messageDTO.OwnerUid = owneruid
        firestore.collection("Chat").document(chooseUid).set(messageDTO)
        val DoRName = chooseUid + "_last"
        firestore.collection("Chat").document(chooseUid).collection("LastMessage").document(DoRName).set(lastmessage)
    }
    private fun joinChat(){ // 유저 체크
        val chooseUid = intent.getStringExtra("contentsUid")!!
        val ownerUid = intent.getStringExtra("owneruid")!!
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val Ref = firestore.collection("Chat").document(chooseUid)
        if(ownerUid != uid){
            firestore.runTransaction { transition ->
                val messageDTO = transition.get(Ref).toObject(MessageDTO::class.java)
                if(!messageDTO!!.UserCheck.containsKey(uid)){
                    messageDTO.UserCheck[uid] = true
                    transition.set(Ref,messageDTO)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun likeupdate(){
        val chooseUid = intent.getStringExtra("contentsUid")!!
        val DoR = firestore.collection("Board").document(chooseUid)
        firestore.runTransaction { transition ->
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val boardDTO = transition.get(DoR).toObject(BoardDTO::class.java)

            // 좋아요 버튼이 클릭되었을때, 취소하는 이벤트
            if(boardDTO!!.like.containsKey(uid)) {
                boardDTO.likeCount -= 1
                boardDTO.like.remove(uid)
                BoardCheck_like.setImageResource(R.drawable.favorite_border)
            }else{// 좋아요 버튼이 클릭되지 않았기때문에 클릭되는 이벤트
                boardDTO.likeCount += 1
                boardDTO.like[uid] = true
                BoardCheck_like.setImageResource(R.drawable.favorite)
            }
            transition.set(DoR,boardDTO )
            board_likeCount.text = "like : " + boardDTO.likeCount.toString()
        }

    }


    @SuppressLint("SetTextI18n")
    override fun loadPage(noti: BoardDTO) {
        val nickname: TextView = findViewById(R.id.boradCheck_nickname)
        val profile: ImageView = findViewById(R.id.boardCheck_profile)
        val title: TextView = findViewById(R.id.boradCheck_title)
        val date: TextView = findViewById(R.id.boradCheck_date)
        val contents: TextView = findViewById(R.id.boardCheck_contents)
        val expain: TextView = findViewById(R.id.board_explain)
        val boardimage: ImageView = findViewById(R.id.boardCheck_image)
        val likeCount : TextView = findViewById(R.id.board_likeCount)

        Glide.with(this).load(noti.ProfileUrl).into(profile)
        if (noti.imageUrlWrite != null) {
            Glide.with(this).load(noti.imageUrlWrite).into(boardimage)
            // 여기 레이아웃 set
            expain.text = noti.imageWriteExplain.toString()
            boardCheck_image_layout.visibility = View.VISIBLE
        }else{
            boardimage.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        }
        contents.text = noti.contents.toString()
        date.text = noti.Writed_date.toString()
        title.text = noti.postTitle.toString()
        nickname.text = noti.nickname.toString()
        likeCount.text = "like : " + noti.likeCount.toString()
    }

    override fun getData(listener: PostListener, chooseUid: String) {
        val callback = listener
        firestore.collection("Board").document(chooseUid).get()
            .addOnSuccessListener { task ->
                boarddto = task?.toObject(BoardDTO::class.java)!!
                callback.loadPage(boarddto)
            }

    }


}