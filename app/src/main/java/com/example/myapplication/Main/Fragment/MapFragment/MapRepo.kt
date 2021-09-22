package com.example.myapplication.Main.Fragment.MapFragment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MapRepo {
    private var savedLocation = arrayListOf<String>()
    private var firestore = FirebaseFirestore.getInstance()
    private var log = ArrayList<Double>()
    private var lat = ArrayList<Double>()

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
            }
    }

    //
    fun getLocations(aaa: ArrayList<String>) {
        for (i in aaa) {
            firestore.collection("Board").document(i).get()?.addOnSuccessListener {
                if (it != null) {
                    log.add(it["longitude"] as Double)
                    lat.add(it["latitude"] as Double)
                }
            }
        }
    }

    //위도 경도 리턴띠
    fun returnLongitude(): ArrayList<Double> {
        return log
    }

    fun returnLatitude(): ArrayList<Double> {
        return lat
    }


}