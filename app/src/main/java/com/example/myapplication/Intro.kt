package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.DTO.Util
import com.example.myapplication.Login.LoginActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class Intro : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION)
    private var util = Util.StaticFunction.getInstance()
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var locationName: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if(checkPermissions()) {
            val thread = Thread(Runnable {
                getLoginLocation()
                Thread.sleep(3000)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }).start()
        }
        else{
            val thread = Thread(Runnable {

                val intent = Intent(this, PermissionActivity::class.java)
                startActivity(intent)
                Thread.sleep(3000)
                finish()
            }).start()
        }
    }
    //퍼미션 체크 및 권한 요청 함수
    private fun checkPermissions() :Boolean {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
              return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun getLoginLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location!!.latitude
                longitude = location.longitude
                locationName = getCityName(latitude!!,longitude!!)
                util.setLatitude(latitude!!)
                util.setLongitude(longitude!!)
                util.setCityName(locationName!!)
            }


    }
    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var doName: String = ""

        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)

        cityName = Adress.get(0).locality
        doName = Adress.get(0).thoroughfare


        //Toast.makeText(context, cityName + " " + doName + " " + jibunName, Toast.LENGTH_LONG).show()
        return "$cityName, $doName"
    }

}