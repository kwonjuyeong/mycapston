/*package com.example.myapplication.Main.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ContactUsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mView: MapView

    companion object {
        fun newInstance(): ContactUsFragment {
            return ContactUsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.activity_google_maps, container, false)

        mView = rootView.findViewById(R.id.map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val seoul = LatLng(37.654601, 127.060530)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

        //마커 출력
        val marker = MarkerOptions()
            .position(seoul)
            .title("Korea,seoul")
            .snippet("서울")
        googleMap.addMarker(marker)
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
}*/