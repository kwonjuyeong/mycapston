package com.example.myapplication


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.gms.maps.model.LatLng
import java.util.*

class ChatMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 앱 구동시 LoginFragment 표시
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.layout_frame, LoginFragment())
//            .commit()

    }

    // ChatFragment로 프래그먼트 교체 (LoginFragment에서 호출할 예정)
//    fun replaceFragment(bundle: Bundle) {
//
//        destination.arguments = bundle      // 닉네임을 받아옴
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.layout_frame, destination)
//            .commit()
//    }
}