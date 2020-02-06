package com.example.taxitestapp.utils

import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.lang.NumberFormatException

fun String.parseLocation(): LatLng? {
    val parts = this.split(':')
    try {
        return LatLng(parts[0].toDouble(), parts[1].toDouble())
    } catch (e: NumberFormatException) {
        Log.e("parseLocation", "Wrong value", e)
    }
    return null
}

fun Location.string(): String {
    return String.format("%.6f:%.6f", this.latitude, this.longitude)
}