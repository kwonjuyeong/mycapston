package com.example.myapplication.DTO

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Main.Fragment.MapFragment.DistanceObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Util() {
    object StaticFunction {
        private var instance: Util? = null
        fun getInstance(): Util {
            if (instance == null)
                instance = Util()
            return instance!!
        }
    }

    private val R = 6372.8 * 1000

    private var longitude: Double? = null
    private var latitude: Double? = null
    private var locationName: String? = null
    private var liveHotData = MutableLiveData<MutableList<BoardDTO>>()
    private var HotList = mutableListOf<BoardDTO>()
    private var uidList = mutableListOf<String>()
    private var liveUidList = MutableLiveData<MutableList<String>>()
    private var firestore = FirebaseFirestore.getInstance()
    private var res = mutableListOf<Int>()

    fun setLongitude(log: Double) {
        longitude = log
    }

    fun setLatitude(lat: Double) {
        latitude = lat
    }

    fun setCityName(City: String) {
        locationName = City
    }

    fun returnCityName(): String {
        return locationName!!
    }

    fun returnLongitude(): Double {
        return longitude!!
    }

    fun returnLatitude(): Double {
        return latitude!!
    }

    fun getAroundHot(): LiveData<MutableList<BoardDTO>> {
        HotList.clear()
        val hef = firestore.collection("Board").whereEqualTo("locationName", locationName).orderBy("likeCount",
            Query.Direction.DESCENDING)
        hef.addSnapshotListener { value, error ->
            for (document in value!!.documents) {
                val item = document.toObject(BoardDTO::class.java)
                val result = DistanceObject.getDistance(latitude!!, longitude!!, item!!.latitude!!, item.longitude!!)
                if (result < 2000) {
                    HotList.add(item)
                    liveHotData.value = HotList
                }
            }
        }
        return  liveHotData
    }
    fun getAroundId():LiveData<MutableList<String>> {
        uidList.clear()
        val hef = firestore.collection("Board").whereEqualTo("locationName", locationName).orderBy(
            "likeCount",
            Query.Direction.DESCENDING
        )
        hef.addSnapshotListener { value, error ->
            for (document in value!!.documents) {
                val item = document.toObject(BoardDTO::class.java)
                val result = DistanceObject.getDistance(
                    latitude!!,
                    longitude!!,
                    item!!.latitude!!,
                    item.longitude!!
                )
                if (result < 2000) {
                    uidList.add(document!!.id)
                    liveUidList.value = uidList
                }
            }
        }
        return liveUidList
    }
}
/*
    fun qsort(array: MutableList<Int>, left: Int = 0, right: Int = array.size - 1) {
        val index = partition(array, left, right)
        if (left < index - 1) {
            qsort(array, left, index - 1)
        }
        if (index < right) {
            qsort(array, index, right)
        }
    }

    fun partition(array: MutableList<Int>, start: Int, end: Int): Int {
        var left = start
        var right = end
        val pivot = array[(left + right) / 2]

        while (left <= right) {
            while (array[left] < pivot) {
                left++
            }

            while (array[right] > pivot) {
                right--
            }

            if (left <= right) {
                val temp = array[left]
                array[left] = array[right]
                array[right] = temp
                left++
                right--
            }
        }
        return left
    }*/



