package com.example.myapplication.Main.Fragment.MapFragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.DTO.BoardDTO
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MapRepo {
    private var savedMapdata = mutableListOf<BoardDTO>()
    private var firestore = FirebaseFirestore.getInstance()
    private var mapIntentUid = mutableListOf<String>()


    object StaticFunction {
        private var instance: MapRepo? = null


        fun getInstance(): MapRepo {
            if (instance == null)
                instance = MapRepo()
            return instance!!
        }
    }

    // 메인에서 실행
    fun LoadLocation() {
        firestore.collection("Board").orderBy("timestamp")
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    val item = snapshot.toObject(BoardDTO::class.java)
                    savedMapdata.add(item!!)
                    mapIntentUid.add(snapshot.id)
                }

                /*getgender(savedLocation)*/

            }
    }
    fun returnMapdata() : MutableList<BoardDTO>{
        return savedMapdata
    }
    fun returnIntentUid() : MutableList<String>{
        return  mapIntentUid
    }
//
//    fun getLocations(aaa: MutableList<String>) {
//        for (i in aaa) {
//            firestore.collection("Board").document(i).get()?.addOnSuccessListener {
//                if (it != null) {
//                    log.add(it["longitude"] as Double)
//                    lat.add(it["latitude"] as Double)
//                    nickname.add(it["nickname"] as String)
//                    gender.add(it["gender"] as String)
//                }
//            }
//        }
//    }
//
//    fun getImage(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    photoUrl.add(it["profileUrl"] as String)
//                }
//            }
//        }
//    }
//
//    fun getnickname(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    nickname.add(it["nickname"] as String)
//                }
//            }
//        }
//    }
//
//    fun getcontent(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    contents.add(it["contents"] as String)
//                }
//            }
//        }
//    }
//
//    fun getdate(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    writed_date.add(it["writed_date"] as String)
//                }
//            }
//        }
//    }
//
//    fun gettitle(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    title.add(it["postTitle"] as String)
//                }
//            }
//        }
//    }
//
//   /* fun getgender(boardDTOId: MutableList<String>) {
//        for (i in boardDTOId) {
//            firestore.collection("Board").document(i).get().addOnSuccessListener {
//
//                if (it != null) {
//                    gender.add(it["gender"] as String)
//                }
//            }
//        }
//    }
//*/
//
//
//    fun returnImage(): MutableList<String> {
//        return photoUrl
//    }
//
//    //위도 경도 리턴띠
//    fun returnLongitude(): MutableList<Double> {
//        return log
//    }
//
//    fun returnLatitude(): MutableList<Double> {
//        return lat
//    }
//
//    fun returnnickname(): MutableList<String>{
//        return nickname
//    }
//
//    fun returncontents(): MutableList<String>{
//        return contents
//    }
//
//    fun returndate(): MutableList<String>{
//        return writed_date
//    }
//
//    fun returntitle(): MutableList<String>{
//        return title
//    }
//
//    fun returngender(): MutableList<String>{
//        return gender
//    }
//

}
