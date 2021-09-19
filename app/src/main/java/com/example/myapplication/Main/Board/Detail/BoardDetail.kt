package com.example.myapplication.Main.Board.Detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Board.Detail.Comment.BoardComment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_detail.*


class BoardDetail : AppCompatActivity(), PostListener {
    private var boarddto = BoardDTO()
    private var firestore = FirebaseFirestore.getInstance()
    val chooseUid: String? = null
    var uid : String? =null

    //    private val nickname : TextView = findViewById(R.id.boradCheck_nickname)
//    private val profile : ImageView = findViewById(R.id.boardCheck_profile)
//    private val title : TextView = findViewById(R.id.boradCheck_title)
//    private val date : TextView = findViewById(R.id.boradCheck_date)
//    private val contents : TextView = findViewById(R.id.boardCheck_contents)
//    private val expain : TextView = findViewById(R.id.board_explain)
//    private val boardimage : ImageView = findViewById(R.id.boardCheck_image)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
//        getData(this, chooseUid,)

        val chooseUid = intent.getStringExtra("contentsUid")!!

        getData(this, chooseUid)
        BoardCheck_like.setOnClickListener{
            likeupdate()

        }
        BoardCheck_commend.setOnClickListener{
            intent.putExtra("chooseUid", chooseUid)
            startActivity(Intent(this,BoardComment::class.java))
        }
        BoardCheck_commend.setOnClickListener{
            val intent = Intent(this, BoardComment::class.java)
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

    fun likeupdate(){
        val chooseUid = intent.getStringExtra("contentsUid")!!
        var DoR = firestore.collection("Board").document(chooseUid!!)
        firestore.runTransaction { transition ->
            var uid = FirebaseAuth.getInstance().currentUser!!.uid
            var boardDTO = transition.get(DoR).toObject(BoardDTO::class.java)

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