package com.example.myapplication.Main.Fragment.MapFragment

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MapRepo {
    private var savedLocation = mutableListOf<String>()
    private var firestore = FirebaseFirestore.getInstance()
    private var log = mutableListOf<Double>()
    private var lat = mutableListOf<Double>()
    private var photoUrl = mutableListOf<String>()

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
                }
            }
        }
    }





        fun getImage(boardDTOId: MutableList<String>){
            for (i in boardDTOId){
                firestore.collection("Board").document(i).get().addOnSuccessListener {

                    if (it != null) {
                        photoUrl.add(it["profileUrl"] as String)
                    }
                }
            }
        }



    fun returnImage() : MutableList<String>{
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