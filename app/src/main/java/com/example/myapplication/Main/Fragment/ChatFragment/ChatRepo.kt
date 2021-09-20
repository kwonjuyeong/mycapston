package com.example.myapplication.Main.Fragment.ChatFragment

import com.example.myapplication.Main.Fragment.BoardFragment.repo.Repo
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepo {
    private var firestore = FirebaseFirestore.getInstance()

    object StaticFunction {
        private var instance: ChatRepo? = null
        fun getInstance(): ChatRepo {
            if (instance == null)
                instance = ChatRepo()
            return instance!!
        }
    }


}