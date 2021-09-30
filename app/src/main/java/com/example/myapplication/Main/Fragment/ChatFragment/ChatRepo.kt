package com.example.myapplication.Main.Fragment.ChatFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.DTO.MessageDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepo {
    private var firestore = FirebaseFirestore.getInstance()
    private var userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private var messageDTO = mutableListOf<MessageDTO>()
    private var lastMessageDTO = mutableListOf<MessageDTO.lastMessage>()
    private var liveLastData = MutableLiveData<MutableList<MessageDTO.lastMessage>>()
    //private var liveLatestMessages = MutableLiveData<MutableList<MessageDTO.lastMessage>>

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
                    val temp = document.toObject(MessageDTO::class.java)
                    messageDTO.add(temp!!)
                    Log.e("메세지 리스트 확인", messageDTO.toString())
                }
            }
    }

//    fun getLastMessage() : MutableLiveData<MutableList<MessageDTO.lastMessage>>{
//        for (i in messageDTO) {
//            var docName = i.boardUid + "_last"
//            firestore.collection("Chat").document(i.boardUid.toString()).collection("LastMessage")
//                .document(docName).get().addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        var last = it.result.toObject(MessageDTO.lastMessage::class.java)
//                        lastMessageDTO.value
//                    }
//                }
//        }
//
//    }

    fun returnLastMessageDTO(): LiveData<MutableList<MessageDTO.lastMessage>> {
        for (i in messageDTO) {
            val docName = i.boardUid + "_last"
            Log.e("returnLastme", docName)
            firestore.collection("Chat").document(i.boardUid.toString()).collection("LastMessage")
                .document(docName).get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val item = it.result.toObject(MessageDTO.lastMessage::class.java)
                        lastMessageDTO.add(item!!)
                        Log.e("returnLastMessageDTO", lastMessageDTO.toString())
                        liveLastData.value = lastMessageDTO
                    }
                }
        }
        return liveLastData
    }
}
//    fun returnLastMessageDTO(): LiveData<MutableList<MessageDTO.lastMessage>> {
//        for (i in messageDTO){
//            val docName = i.boardUid + "_last"
//            Log.e("returnLastme", docName )
//            firestore.collection("Chat").document(i.boardUid.toString()).collection("LastMessage")
//                .document(docName).addSnapshotListener { value, error ->
//                    if(value == null) return@addSnapshotListener
//                    val item = value.toObject(MessageDTO.lastMessage::class.java)
//                    lastMessageDTO.add(item!!)
//                    Log.e("returnLastMessageDTO", lastMessageDTO.toString() )
//                    liveLastData.value = lastMessageDTO
//                }
//        }
//        return liveLastData
//    }



