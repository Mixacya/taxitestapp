package com.example.taxitestapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Location

object LocationPreferences {

    private const val PREFS_NAME = "location_prefs"
    private const val KEY_LAST_LOCATION = "last_location"
    private const val KEY_DISTANCE = "last_distance"

    fun getLastLocation(context: Context): String {
        val locationSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return locationSettings.getString(KEY_LAST_LOCATION, "") ?: ""
    }

    fun setLastLocation(context: Context, location: Location) {
        val locationSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val locationLine = location.string()
        locationSettings.edit().putString(KEY_LAST_LOCATION, locationLine).apply()
    }

    fun getDistance(context: Context): Float {
        val locationSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return locationSettings.getFloat(KEY_DISTANCE, 0f)
    }

    fun setDistance(context: Context, distance: Float) {
        val locationSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        locationSettings.edit().putFloat(KEY_DISTANCE, distance).apply()
    }

}