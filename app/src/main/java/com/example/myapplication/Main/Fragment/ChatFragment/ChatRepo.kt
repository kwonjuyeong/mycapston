package com.example.myapplication.Main.Fragment.ChatFragment

import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.Main.Fragment.BoardFragment.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepo {
    private var firestore = FirebaseFirestore.getInstance()
    private var userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private var chattingRoomList = mutableListOf<MutableMap<String, Boolean>>()
    private var messageDTO = mutableListOf<MessageDTO>()
    private var lastMessageDTO = mutableListOf<MessageDTO.lastMessage>()

    object StaticFunction {
        private var instance: ChatRepo? = null
        fun getInstance(): ChatRepo {
            if (instance == null)
                instance = ChatRepo()
            return instance!!
        }
    }

    fun CheckChattingRoom() {
        firestore.collection("Chat").whereEqualTo("userCheck", mutableMapOf(userUid to true))
            .addSnapshotListener { value, error ->
                if (value == null) return@addSnapshotListener
                for (document in value.documents) {
                    var temp = document.toObject(MessageDTO::class.java)
                    messageDTO.add(temp!!)
                }
                getLastMessage(messageDTO)
            }

    }

    fun getLastMessage(messageDTO: MutableList<MessageDTO>) {
        for (i in messageDTO) {
            var docName = i.boardUid + "_last"
            firestore.collection("Chat").document(i.boardUid.toString()).collection("LastMessage")
                .document(docName).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        var last = it.result.toObject(MessageDTO.lastMessage::class.java)
                        lastMessageDTO.add(last!!)
                    }
                }
        }
    }

    fun returnLastMessageDTO(): MutableList<MessageDTO.lastMessage> {
        return lastMessageDTO
    }
}