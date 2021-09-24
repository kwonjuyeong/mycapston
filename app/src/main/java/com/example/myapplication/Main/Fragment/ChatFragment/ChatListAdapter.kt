package com.example.myapplication.Main.Fragment.ChatFragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.Main.Fragment.MapFragment.MapRepo
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("NotifyDataSetChanged")
class ChatListAdapter: RecyclerView.Adapter<ChatListAdapter.ChatListHolder>()  {
    private var firestore = FirebaseFirestore.getInstance().collection("Chat")
    private var userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private var lastMessageDTO = mutableListOf<MessageDTO.lastMessage>()
    private var chatRepo = ChatRepo()
    init {
        chatRepo = ChatRepo.StaticFunction.getInstance()
        lastMessageDTO = chatRepo.returnLastMessageDTO()
        notifyDataSetChanged()
    }
    class ChatListHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate()
        return ChatListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}