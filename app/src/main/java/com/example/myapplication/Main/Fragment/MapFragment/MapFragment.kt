package com.example.myapplication.Main.Fragment.MapFragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_now_my_place.*
import kotlinx.android.synthetic.main.custom_marker.*
import kotlinx.android.synthetic.main.frag_board.*
import kotlinx.android.synthetic.main.frag_setting.*
import kotlinx.android.synthetic.main.view_item_layout.*
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private var client: FusedLocationProviderClient? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010
    private lateinit var mView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var maprepo = MapRepo.StaticFunction.getInstance()

    private lateinit var marker12: ImageView


    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        //RequestPermission()

        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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

        val sub_view = inflater.inflate(R.layout.custom_marker, container, false)
        marker12 = sub_view.findViewById(R.id.markers1) as ImageView

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        return main_view
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
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
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
        var jibunName: String = ""

        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat, long, 3)

        //countryName = Adress.get(0).countryName
        cityName = Adress.get(0).locality
        doName = Adress.get(0).thoroughfare
        jibunName = Adress.get(0).featureName

        Toast.makeText(context, cityName + " " + doName + " " + jibunName, Toast.LENGTH_LONG)
            .show()
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
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
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

    // lifecycleScope - 쓰는 방법을 알아야함.
     /*fun getBitmap(url: String): Bitmap? {

        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
           val input = connection.inputStream
           val bitmap = BitmapFactory.decodeStream(input)
    return bitmap
        }catch (e:IOException){
        }
        return null
   }*/


    /*
    private suspend fun getBitmap(url: String): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = coil.request.ImageRequest.Builder(requireContext()).data(url).build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }*/




    private fun otherUserMaker(googleMap: GoogleMap) {
        //다른 사용자들 마커 찍기
        var latitude = mutableListOf<Double>()
        var longitude = mutableListOf<Double>()
        var user_URL = mutableListOf<String>()

        user_URL = maprepo.returnImage()
        latitude = maprepo.returnLatitude()
        longitude = maprepo.returnLongitude()

        for (i in 0 until latitude.size step (1)) {

           var img = Picasso.get().load(user_URL[i]).into(marker12)


            /*if(user_URL[i] != null) {

                    val makerOptions = MarkerOptions()
                    makerOptions
                        .position(LatLng(latitude[i], longitude[i]))
                        .title("닉네임$i") //카드뷰로 대체

                }else {*/
            val makerOptions = MarkerOptions()
            makerOptions
                .position(LatLng(latitude[i], longitude[i]))
                .title("닉네임$i") //카드뷰로 대체
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                //.icon(BitmapDescriptorFactory.fromResource(R.id.markers1))
            googleMap.addMarker(makerOptions)

        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                var myLocation = location?.let { LatLng(it.latitude, it.longitude) }

                //초기 값 설정(주변 위치로 나옴)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.0, 127.0)))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                val marker = MarkerOptions()
                    .position(LatLng(37.0, 127.0))

                //otherUserMaker(googleMap)

                    //현재위치 최신화 버튼을 누르면 현재 위치가 뜸
                    recent_button.setOnClickListener {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                        val marker = MarkerOptions()
                            .position(myLocation)
                            .title("현재 위치")
                            .snippet("입니다.")
                        googleMap.addMarker(marker)

                        otherUserMaker(googleMap)
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
/*package com.example.myapplication.Main.Fragment.MapFragment


import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.Image
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.loader.content.AsyncTaskLoader
import coil.ImageLoader
import coil.request.SuccessResult
import com.example.myapplication.R
import com.facebook.internal.ImageRequest
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_now_my_place.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private var client: FusedLocationProviderClient? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010
    private lateinit var mView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var maprepo = MapRepo.StaticFunction.getInstance()

    private var bitmapList = mutableListOf<Bitmap>()



    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        //RequestPermission()

        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        }


        override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



            val main_view = inflater.inflate(R.layout.activity_now_my_place, container, false)
            lifecycleScope.launch(Dispatchers.IO){
               val bitmap = getBitmap(url = String())
            }


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
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_ID
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
            var jibunName: String = ""

            var geoCoder = Geocoder(requireContext(), Locale.getDefault())
            var Adress = geoCoder.getFromLocation(lat, long, 3)

            //countryName = Adress.get(0).countryName
            cityName = Adress.get(0).locality
            doName = Adress.get(0).thoroughfare
            jibunName = Adress.get(0).featureName

            Toast.makeText(context, cityName + " " + doName + " " + jibunName, Toast.LENGTH_LONG)
                .show()
            return cityName
        }


    // lifecycleScope - 쓰는 방법을 알아야함.
     fun getBitmap(url: String): Bitmap? {

        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            return bitmap
        }catch (e:IOException){

        }
        return null
    }


/*
    private suspend fun getBitmap(url : String):Bitmap{
        val loading = ImageLoader(requireContext())
        val request = coil.request.ImageRequest.Builder(requireContext()).data(url).build()

        val result = (loading.execute(request)as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
*/


        private fun otherUserMaker(googleMap: GoogleMap){




            //다른 사용자들 마커 찍기
            var latitude = mutableListOf<Double>()
            var longitude = mutableListOf<Double>()
            var user_URL = mutableListOf<String>()

            //type = ArrayList<Double>
            latitude = maprepo.returnLatitude()
            longitude = maprepo.returnLongitude()
            user_URL = maprepo.returnImage()
           // Glide.with(this).load(user_URL[i]).into(maker1)

                for(i in 0 until latitude.size step (1)){

                    //user_URL[i]
                        /*if(user_URL[i] != null) {

                            val makerOptions = MarkerOptions()
                            makerOptions
                                .position(LatLng(latitude[i], longitude[i]))
                                .title("닉네임$i") //카드뷰로 대체

                        }else {*/

                            val makerOptions = MarkerOptions()
                            makerOptions
                                .position(LatLng(latitude[i], longitude[i]))
                                .title("닉네임$i") //카드뷰로 대체
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(user_URL[i])))

                    Log.e("확인4", latitude[i].toString())
                    Log.e("확인5", longitude[i].toString())
                    Log.e("확인6", user_URL[i])

                    googleMap?.addMarker(makerOptions)
                }
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


                //초기 값 설정(주변 위치로 나옴)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.0,127.0)))
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
            val marker = MarkerOptions()
                .position(LatLng(37.0,127.0))
            googleMap?.addMarker(marker)


            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    var myLocation = location?.let { LatLng(it.latitude, it.longitude) }


                    //현재위치 최신화 버튼을 누르면 현재 위치가 뜸
                    recent_button.setOnClickListener {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                        val marker = MarkerOptions()
                            .position(myLocation)
                            .title("현재 위치")
                            .snippet("입니다.")
                        googleMap?.addMarker(marker)

                        otherUserMaker(googleMap)
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

*/