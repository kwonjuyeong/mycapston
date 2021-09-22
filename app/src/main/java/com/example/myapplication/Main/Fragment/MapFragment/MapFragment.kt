package com.example.myapplication.Main.Fragment.MapFragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location

import com.google.android.gms.location.*
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.myapplication.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_now_my_place.*
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {
    private var client: FusedLocationProviderClient? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010
    private lateinit var mView: MapView
    private lateinit var googleMap: GoogleMap


    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
    init {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val main_view = inflater.inflate(R.layout.activity_now_my_place, container, false)

        mView = main_view.findViewById(R.id.realtime_map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        return main_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            RequestPermission()
            getLastLocation()

    }

    fun CheckPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun RequestPermission() {
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        Log.e("1번", "1")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled(): Boolean {
        var locationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        Log.d("Debug:", "Your Location:" + location.longitude)
                        Log.e(
                            "씨",
                            "You Current Location is : Long: " + location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Turn on Your device Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {

        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        //var countryName = ""
        var cityName: String = ""
        var doName: String = ""
        var jibunName:String = ""

        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat, long, 3)

        //countryName = Adress.get(0).countryName
        cityName = Adress.get(0).locality
        doName = Adress.get(0).thoroughfare
        jibunName = Adress.get(0).featureName

        Toast.makeText(context, cityName+" "+doName+" "+jibunName , Toast.LENGTH_LONG).show()
        return cityName
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.e("Debug:", "your last last location: " + lastLocation.longitude.toString())
            Log.e(
                "위도 경",
                "You Last Location is : Long: " + lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(
                    lastLocation.latitude, lastLocation.longitude
                )
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Debug:", "You have the Permission")
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                var myLocation =
                    location?.let {
                        com.google.android.gms.maps.model.LatLng(
                            it.latitude,
                            it.longitude
                        )
                    }
                Log.e("확인", myLocation?.latitude.toString() )
                Log.e("확인", myLocation?.longitude.toString() )

                //초기 값 설정(주변 위치로 나옴)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                val marker = MarkerOptions()
                    .position(myLocation)
                    .title("초기값")
                    .snippet("설정")
                googleMap?.addMarker(marker)

                //현재위치 최신화 버튼을 누르면 현재 위치가 뜸
                recent_button.setOnClickListener {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                    val marker = MarkerOptions()
                        .position(myLocation)
                        .title("현재 위치")
                        .snippet("입니다.")
                    googleMap?.addMarker(marker)
                }
            }
    }
    override fun onStart() {
        super.onStart()
        mView.onStart()
    }
    override fun onStop() {
        super.onStop()
        mView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }
}
/*
val myLocation = com.google.android.gms.maps.model.LatLng(lnt, lot)
//왜 0.0, 0.0이 뜨는거지?
Log.e("sex","${lnt} ${lot}")

recent_button.setOnClickListener {
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
    googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    val marker = MarkerOptions()
    .position(myLocation)
    .title("현재 위치")
    .snippet(getCityName(lnt, lot)+"입니다.")
    googleMap?.addMarker(marker)
}
}

override fun onStart() {
super.onStart()
mView.onStart()
}
override fun onStop() {
super.onStop()
mView.onStop()
}
override fun onResume() {
super.onResume()
mView.onResume()
}
override fun onPause() {
super.onPause()
mView.onPause()
}
override fun onLowMemory() {
super.onLowMemory()
mView.onLowMemory()
}
override fun onDestroy() {
mView.onDestroy()
super.onDestroy()
}

}

//    private fun location() {
//        LocationManager locationManager = (LocationManager) getActivity()
//    }
*/