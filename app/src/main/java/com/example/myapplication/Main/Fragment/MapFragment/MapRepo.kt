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
    private var savedLocation = mutableListOf<String>()
    private var firestore = FirebaseFirestore.getInstance()
    private var log = mutableListOf<Double>()
    private var lat = mutableListOf<Double>()
    private var photoUrl = mutableListOf<String>()
    private var nickname = mutableListOf<String>()
    private var gender = mutableListOf<String>()
    private var likecount = mutableListOf<Int>()
    private var writed_date = mutableListOf<String>()
    private var title = mutableListOf<String>()
    private var contents = mutableListOf<String>()


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
                    savedLocation.add(snapshot.id)
                }
                getLocations(savedLocation)
                getImage(savedLocation)
            }
    }

    fun getLocations(aaa: MutableList<String>) {
        for (i in aaa) {
            firestore.collection("Board").document(i).get()?.addOnSuccessListener {
                if (it != null) {
                    log.add(it["longitude"] as Double)
                    lat.add(it["latitude"] as Double)
                    nickname.add(it["nickname"] as String)
                    gender.add(it["gender"] as String)
                    likecount.add(it["likecount"]as Int)

                }
            }
        }
    }

    fun getImage(boardDTOId: MutableList<String>) {
        for (i in boardDTOId) {
            firestore.collection("Board").document(i).get().addOnSuccessListener {

                if (it != null) {
                    photoUrl.add(it["profileUrl"] as String)
                }
            }
        }
    }
/*
    fun getBitmap(url: MutableList<String>) {
        for (i in url) {
            try {
                val url = URL(i)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                user_Url.add(bitmap)
            } catch (e: IOException) {
            }
        }
    }*/

    fun returnImage(): MutableList<String> {
        return photoUrl
    }


    //위도 경도 리턴띠
    fun returnLongitude(): MutableList<Double> {
        return log
    }

    fun returnLatitude(): MutableList<Double> {
        return lat
    }


}
