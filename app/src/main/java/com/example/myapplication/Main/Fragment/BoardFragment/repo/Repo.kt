package com.example.myapplication.Main.Fragment.BoardFragment.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.DTO.BoardDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.frag_board.*
import kotlin.concurrent.thread

class Repo {
    // 초기값 백업1
    private var livedata: MutableLiveData<BoardDTO>? = null
    private var liveboarduid: MutableLiveData<String>? = null
    private var firestore = FirebaseFirestore.getInstance()
    private var listdata = mutableListOf<BoardDTO>()
    private var contentsuid = arrayListOf<String>()

    object StaticFunction {
        private var instance: Repo? = null
        fun getInstance(): Repo {
            if (instance == null)
                instance = Repo()
            return instance!!
        }
    }
    init {
        listdata.clear()
        contentsuid.clear()
        firestore.collection("Board").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BoardDTO::class.java)
                    listdata.add(item!!)
                    contentsuid.add(snapshot.id)
                }
            }

    }





    fun getboarddata(): MutableList<BoardDTO> {
//        firestore.collection("Board").orderBy("timestamp")
//            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
//                if (querySnapshot == null) return@addSnapshotListener
//                for (snapshot in querySnapshot!!.documents) {
//                    var item = snapshot.toObject(BoardDTO::class.java)
//                    listdata.add(item!!)
        Log.e("Repo listdata ", listdata.size.toString() )
        return listdata
    }

    fun getboardUid(): ArrayList<String> {
//        firestore.collection("Board").orderBy("timestamp")
//            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
//                if (querySnapshot == null) return@addSnapshotListener
//                for (snapshot in querySnapshot!!.documents) {
//                    contentsuid.add(snapshot.id)
//                }
//            }
        return contentsuid
    }

    fun getListdata(): LiveData<BoardDTO> {
        //listdata.clear()
        livedata = MutableLiveData<BoardDTO>()    // 객체 생
        if (listdata != null)
            listdata.clear()
        firestore.collection("Board").orderBy("timestamp",Query.Direction.DESCENDING)
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BoardDTO::class.java)
                    livedata!!.postValue(item!!)
                }
            }
        return livedata!!
    }

    fun getlistuid(): LiveData<String> {
        //contentsuid.clear()
        if(contentsuid != null)
            contentsuid.clear()
        liveboarduid = MutableLiveData<String>()
        firestore.collection("Board").orderBy("timestamp",Query.Direction.DESCENDING)
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    liveboarduid!!.postValue(snapshot.id)
                }
            }
        return liveboarduid!!
    }

}






