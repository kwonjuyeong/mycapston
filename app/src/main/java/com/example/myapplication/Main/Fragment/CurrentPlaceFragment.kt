package com.example.myapplication.Main.Fragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import com.example.myapplication.R
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import com.google.android.gms.maps.model.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CurrentPlaceFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val UPDATE_INTERVAL_MS = 1000 * 60 * 15 // 1분 단위 시간 갱신
        private const val FASTEST_UPDATE_INTERVAL_MS = 1000 * 30 // 30초 단위로 화면 갱신
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        const val TAG: String = "로그"
        fun newInstance(): CurrentPlaceFragment {
            return CurrentPlaceFragment()
        }
    }

    private val TAG = this.javaClass.simpleName
    private lateinit var mContext: Context
    private lateinit var mMap: GoogleMap
    private var currentMarker: Marker? = null

    private lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var mCurrentLocatiion: Location? = null
    private var mCameraPosition: CameraPosition? = null
    private val mDefaultLocation = LatLng(37.56, 126.97)
    private var mLocationPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let{
            mCurrentLocatiion = it.getParcelable(KEY_LOCATION)
            mCameraPosition = it.getParcelable(KEY_CAMERA_POSITION)
        }

        mContext = requireContext()

        locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // 정확도를 최우선적으로 고려
            .setInterval(UPDATE_INTERVAL_MS.toLong()) // 위치가 Update 되는 주기
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong()) // 위치 획득후 업데이트되는 주기
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)

        // Build the map. - Fragment에서는 필요없음
        //val mapFragment =findFragmentById(R.id.realtime_map) as SupportMapFragment
        //mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var main_view = inflater.inflate(R.layout.activity_now_my_place,container,false)

        return main_view
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        mMap.let{
            outState.putParcelable(KEY_CAMERA_POSITION, it.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mCurrentLocatiion)
            super.onSaveInstanceState(outState)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        Log.e(TAG, "onMapReady :")
        mMap = map
        setDefaultLocation() // GPS를 찾지 못하는 장소에 있을 경우 지도의 초기 위치가 필요함.
        locationPermission
        updateLocationUI()
        deviceLocation
    }

    private fun updateLocationUI() {
        mMap.let{
            try {
                if (mLocationPermissionGranted) {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    it.isMyLocationEnabled = false
                    it.uiSettings.isMyLocationButtonEnabled = false
                    mCurrentLocatiion = null
                    locationPermission
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message!!)
            }
        }
    }

    private fun setDefaultLocation() {
        currentMarker?.remove()
        val markerOptions = MarkerOptions()
        markerOptions.position(mDefaultLocation)
        markerOptions.title("위치정보 가져올 수 없음")
        markerOptions.snippet("위치 퍼미션과 GPS 활성 여부 확인하세요")
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        currentMarker = mMap.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15f)
        mMap.moveCamera(cameraUpdate)
    }

    fun getCurrentAddress(latlng: LatLng): String {
        // 위치 정보와 지역으로부터 주소 문자열을 구한다.
        var addressList: List<Address>?
        val geocoder =
            Geocoder(mContext, Locale.getDefault())

        // 지오코더를 이용하여 주소 리스트를 구한다.
        addressList = try {
            geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
        } catch (e: IOException) {
            Toast.makeText(
                mContext,
                "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
            return "주소 인식 불가"
        }
        if (addressList != null) {
            if (addressList.isEmpty()) { // 주소 리스트가 비어있는지 비어 있으면
                return "해당 위치에 주소 없음"
            }
        }

        // 주소를 담는 문자열을 생성하고 리턴
        val address = addressList?.get(0)
        val addressStringBuilder = StringBuilder()
        if (address != null) {
            for (i in 0..address.maxAddressLineIndex) {
                addressStringBuilder.append(address.getAddressLine(i))
                if (i < address.maxAddressLineIndex) addressStringBuilder.append("\n")
            }
        }
        return addressStringBuilder.toString()
    }

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList =
                locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                val currentPosition =
                    LatLng(location.latitude, location.longitude)
                val markerTitle = getCurrentAddress(currentPosition)
                val markerSnippet =
                    "위도:" + location.latitude.toString() + " 경도:" + location.longitude
                        .toString()
                Log.d(TAG,"Time :" + currentTime() + " onLocationResult : " + markerSnippet)

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet)
                mCurrentLocatiion = location
            }
        }
    }

    private fun currentTime(): String {
        val today = Date()
        val date = SimpleDateFormat("yyyy/MM/dd")
        val time = SimpleDateFormat("hh:mm:ss a")
        return time.format(today)
    }

    fun setCurrentLocation(
        location: Location,
        markerTitle: String?,
        markerSnippet: String?
    ) {
        if (currentMarker != null) currentMarker!!.remove()
        val currentLatLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        currentMarker = mMap.addMarker(markerOptions)
        mMap.setOnMarkerClickListener {
            val inflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            true
        }
        val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap.moveCamera(cameraUpdate)
    }

    private val deviceLocation: Unit
        private get() {
            try {
                if (mLocationPermissionGranted) {
                    mFusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message!!)
            }
        }

    private val locationPermission: Unit
        private get() {
            if (ContextCompat.checkSelfPermission(mContext.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationPermissionGranted = true
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (mLocationPermissionGranted) {
            Log.d(TAG, "onStart : requestLocationUpdates")
            mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
            mMap?.let{
                it.isMyLocationEnabled = true
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationProviderClient.let{
            it.removeLocationUpdates(locationCallback)
            Log.d(TAG, "onStop : removeLocationUpdates")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mFusedLocationProviderClient.let{
            it.removeLocationUpdates(locationCallback)
            Log.d(TAG, "onDestroy : removeLocationUpdates")
        }
    }
}