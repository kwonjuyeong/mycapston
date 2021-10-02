package com.example.myapplication.Main.Board.Detail.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.KeyboardVisibilityUtils
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_chat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BoardChat : AppCompatActivity() {
    private var firestore = FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Chats = BoardDTO.Chat()
    private var lastMessage = MessageDTO.lastMessage()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils //키보드 움직이기
    private var drawerLayout: DrawerLayout? = null
    private var drawerView: View? = null
    var listener: DrawerLayout.DrawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
        override fun onDrawerOpened(drawerView: View) {}
        override fun onDrawerClosed(drawerView: View) {}
        override fun onDrawerStateChanged(newState: Int) {}
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_chat)
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
            if (it.isSuccessful) {
                currentDTO = it.result?.toObject(UserinfoDTO::class.java)!!
                Log.e("값 받아오는거 확인", currentDTO.toString())
            }
        }
        chatAdapter = ChatAdapter(commentUid)
        comment_recyclerView.apply {

            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
        btn_comment_send?.setOnClickListener {
            // 채팅내용 업로드
            updateChat()
            setLastMessage()
            comment_text.setText("")
        }

        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawerView = findViewById(R.id.drawer) as View
        val btn_open = findViewById<View>(R.id.hamburger) as ImageView
        btn_open.setOnClickListener { drawerLayout!!.openDrawer(drawerView!!) }
        val btn_close = findViewById<View>(R.id.btn_close) as Button
        btn_close.setOnClickListener { drawerLayout!!.closeDrawers() }
        drawerLayout!!.setDrawerListener(listener)
        drawerView!!.setOnTouchListener { v, event -> true }

    }


    private fun updateChat() {
        val commentUid = intent.getStringExtra("commentUid")!!
        Chats.UID = uid
        Chats.message = comment_text.text.toString()
        Chats.userprofile = currentDTO.ProfileUrl
        Chats.userNickname = currentDTO.nickname
        Chats.date = SimpleDateFormat("MM월 dd일").format(Date())
        Chats.timestamp = System.currentTimeMillis()
        firestore.collection("Chat").document(commentUid).collection("Messages").document()
            .set(Chats)
    }

    private fun setLastMessage() {
        val time = SimpleDateFormat("MM월 dd일").format(Date())
        val lastchat = comment_text.text.toString()
        val commentUid = intent.getStringExtra("commentUid")!!
        val docName = commentUid + "_last"

        val DoR = firestore.collection("Chat").document(commentUid)
        DoR.collection("LastMessage").document(docName).update(
            mapOf(
                "boardChatuid" to commentUid,
                "senderuid" to uid,
                "lastContent" to lastchat,
                "time" to time,
                "profileUrl" to currentDTO.ProfileUrl,
                "nickname" to currentDTO.nickname,
                "timeStamp" to System.currentTimeMillis()
            )
        )
        setResult(RESULT_OK)
    }

    override fun onPause() {
        super.onPause()

    }

}