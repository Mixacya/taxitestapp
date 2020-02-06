package com.example.taxitestapp.fragment.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.taxitestapp.R
import com.example.taxitestapp.rxbus.RxBus
import com.example.taxitestapp.rxbus.RxEvent
import com.example.taxitestapp.utils.LocationPreferences
import com.example.taxitestapp.utils.parseLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        val MY_LOCATION_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        return inflater.inflate(R.layout.fragment_map, container, false)

//TODO: DELETE        /*VIEW MODEL FROM GOOGLE*/
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mapFragment.getMapAsync(this)

    }

    override fun onStart() {
        super.onStart()
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                val array = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(array, MY_LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap == null) {
            return
        }
        map = googleMap
        activity?.let { context ->

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                val array = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(array, MY_LOCATION_REQUEST_CODE)
            }

//            var position = LatLng(46.971321, 32.004195)
            val locationLine = LocationPreferences.getLastLocation(context)
            if (!locationLine.isNullOrEmpty()) {
                val position = locationLine.parseLocation()
                if (position != null) {
                    refreshMap(position)
                }
            } else {
                val position = LatLng(46.971321, 32.004195)
                refreshMap(position)
            }
            map.setOnMyLocationButtonClickListener {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    val newPosition = LatLng(location.latitude, location.longitude)
                    refreshMap(newPosition)
                }

                true
            }
            RxBus.listen(RxEvent.EventLocation::class.java).subscribe {
                it.location?.let { locationData ->
                    val location = LatLng(locationData.latitude, locationData.longitude)
                    refreshMap(location)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activity?.let {
                        if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            map.isMyLocationEnabled = true
                        }
                    }
                    return
                }
            }
        }
    }

    private fun refreshMap(position: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(position))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }
}