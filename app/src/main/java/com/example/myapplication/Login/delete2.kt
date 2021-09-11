package com.example.myapplication.Login

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDelete2Binding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*

class delete2 : AppCompatActivity(), OnMapReadyCallback {

    val permission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val PERM_FLAG = 99

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView((R.layout.activity_delete2))
        if (isPermitted()) {
            startProgress()
        } else {
            ActivityCompat.requestPermissions(this, permission, PERM_FLAG)
        }
    }



    //  binding = ActivityMapsBinding.inflate(layoutInflater)
    //   setContentView(binding.root)

    fun isPermitted(): Boolean {
        for (perm in permission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PERMISSION_GRANTED)
                return false
        }
        return true
    }


    fun startProgress() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map123) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fusedLoactionClient = LocationServices.getFusedLocationProviderClient(this)
        setUpdateLocationListener()

/*
        //푸드네임, 좌표
        val foodstreet = LatLng(36.832856, 127.135647)
        val mrpizza = LatLng(36.833501, 127.133878)
        val starbux = LatLng(36.833953, 127.134842)
        val gs25 = LatLng(36.833549, 127.135541)

        //마커아이콘 설정
        var bitmapDrawable: BitmapDrawable
        bitmapDrawable = resources.getDrawable(R.drawable.food) as BitmapDrawable
        // val scaleBitmapDrawable = createScaledBitmap1(bitmapDrawable.bitmap,100,100,false) //아이콘 크기제어
        val discriptor = BitmapDescriptorFactory.fromBitmap(bitmapDrawable.bitmap)

        //마커추가
        mMap.addMarker(MarkerOptions().position(foodstreet).title("foodstreet"))//.icon(discriptor))// change icon shape
        mMap.addMarker(MarkerOptions().position(mrpizza).title("mrpizza"))
        mMap.addMarker(MarkerOptions().position(starbux).title("starbux"))
        mMap.addMarker(MarkerOptions().position(gs25).title("gs25"))


        //카메라 위치및 배율 설정
        val cameraOption = CameraPosition.Builder().target(foodstreet).zoom(17f).build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)
        mMap.moveCamera(camera)
        */
    }

    lateinit var fusedLoactionClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListener() {
        val locationRequest = LocationRequest.create()
        locationRequest.run { priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 100 //현재위치 갱신시간
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for ((i, location) in it.locations.withIndex()) {
                        Log.d("로케이션"," $i ${location.latitude}, ${location.longitude}" )
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLoactionClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLastLocation(location : Location){

        //마커아이콘 설정
        var bitmapDrawable: BitmapDrawable
        bitmapDrawable = resources.getDrawable(R.drawable.chicken) as BitmapDrawable
        // val scaleBitmapDrawable = createScaledBitmap1(bitmapDrawable.bitmap,100,100,false) //아이콘 크기제어
        val discriptor = BitmapDescriptorFactory.fromBitmap(bitmapDrawable.bitmap)

        val myLocation = LatLng(location.latitude, location.longitude)
        val marker = MarkerOptions()
            .position(myLocation)
            .title("현위치")
        // .icon(discriptor)
        val cameraOption = CameraPosition.Builder()
            .target(myLocation)
            .zoom(15.0f)
            .build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)

        //푸드네임, 좌표
        val foodstreet = LatLng(36.832856, 127.135647)
        val mrpizza = LatLng(36.833501, 127.133878)
        val starbux = LatLng(36.833953, 127.134842)
        val gs25 = LatLng(36.833549, 127.135541)



        mMap.clear()//지도초기화
        mMap.addMarker(marker)
        mMap.moveCamera(camera)

        mMap.addMarker(MarkerOptions().position(foodstreet).title("foodstreet"))//.icon(discriptor))// change icon shape
        mMap.addMarker(MarkerOptions().position(mrpizza).title("mrpizza"))
        mMap.addMarker(MarkerOptions().position(starbux).title("starbux"))
        mMap.addMarker(MarkerOptions().position(gs25).title("gs25"))

        //거리반경 원
        val circle = mMap.addCircle(
            CircleOptions()
                .center(myLocation)
                .radius(100.0)//지름
                .strokeWidth(10f)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(128, 255, 0, 0))
                .clickable(true)
        )
        val circle2 = mMap.addCircle(
            CircleOptions()
                .center(myLocation)
                .radius(500.0)
                .strokeWidth(10f)
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(0, 0, 0, 0))
                .clickable(true)
        )

    }


    fun onRequstedPermissionResult(
        requestCode: Int,
        permission: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERM_FLAG -> {
                var check = true
                for (grant in grantResults) {
                    if (grant != PERMISSION_GRANTED) {
                        check = false
                        break
                    }
                }
                if(check){
                    startProgress()
                }else{

                    finish()
                }
            }
        }

    }
}