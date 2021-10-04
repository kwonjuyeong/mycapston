package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_chat.*
import kotlinx.android.synthetic.main.activity_chatadd.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger.global

class BoardChat : AppCompatActivity() {
    private var firestore = FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Chats = BoardDTO.Chat()
    private lateinit var chatAdapter: ChatAdapter
    private var repo = Repo.StaticFunction.getInstance()
    private var drawerLayout: DrawerLayout? = null
    private var drawerView: View? = null
    private lateinit var chatOnlineAdapter: chatOnlineAdapter
    var commentUid: String? = null
    private val viewModel by lazy {
        ViewModelProvider(this).get(OnlinViewModel::class.java)
    }
    var listener: DrawerLayout.DrawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
        override fun onDrawerOpened(drawerView: View) {}
        override fun onDrawerClosed(drawerView: View) {}
        override fun onDrawerStateChanged(newState: Int) {}
    }

    init {
        Log.e("init", "init: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_chat)
        commentUid = intent.getStringExtra("commentUid")!!
        val context = this


        chatOnlineAdapter = chatOnlineAdapter(this, commentUid!!)
        // 해당 게시글 uid를 intent로 받아옴
        firestore.collection("userid").document(uid!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                currentDTO = it.result?.toObject(UserinfoDTO::class.java)!!
            }
        }
        chatAdapter = ChatAdapter(commentUid!!)
        comment_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
        status_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatOnlineAdapter
            observerData()}

        //getChatInfo()
        btn_comment_send?.setOnClickListener {
            // 채팅내용 업로드
            updateChat()
            setLastMessage()
            comment_text.setText("")
        }



        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawerView = findViewById(R.id.drawer) as View
        val btn_open = findViewById<View>(R.id.hamburger) as ImageView
        btn_open.setOnClickListener {
                drawerLayout!!.openDrawer(drawerView!!)
        }
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
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")


    }


//    @SuppressLint("NotifyDataSetChanged")
//    fun getChatInfo(): LiveData<MutableList<UserinfoDTO>> {
//        val statusUser = MutableLiveData<MutableList<UserinfoDTO>>()
//        val statusInfos = mutableListOf<UserinfoDTO>()
//        val firestore = FirebaseFirestore.getInstance()
//        val Uef = firestore.collection("userid")
//        val Cef = firestore.collection("Chat").document(commentUid!!)
//        Cef.addSnapshotListener { value, error ->
//            val document = value?.toObject(MessageDTO::class.java)
//            for (i in document?.UserCheck?.keys!!) {
//                Uef.document(i).addSnapshotListener { value, error ->
//                    val item = value?.toObject(UserinfoDTO::class.java)
//                    Log.e("item 확", item.toString())
//                    statusInfos.add(item!!)
//                    statusUser.value = statusInfos
//                    Log.e("statuslist", statusInfos.toString())
//
//                }
//            }
//
//
//        }
//        return statusUser
//    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        viewModel.getChatOnlineData().observe(this, androidx.lifecycle.Observer {
            chatOnlineAdapter.setDataOnlineAdapter(it)
            chatOnlineAdapter.notifyDataSetChanged()
        })
    }

}