package com.example.taxitestapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.IBinder
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taxitestapp.R
import com.example.taxitestapp.rxbus.RxBus
import com.example.taxitestapp.rxbus.RxDistance
import com.example.taxitestapp.rxbus.RxEvent
import com.example.taxitestapp.utils.LocationPreferences
import java.lang.Exception
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


private const val TAG = "GpsService"

class GpsService : Service(), LocationListener {

    companion object {
        const val CHANNEL_ID = "GpsChannelId"
        const val EARTH_RADIUS = 3958.75
        const val METER_CONVERSATION = 1609
        const val MIN_ANGLE_BETWEEN_ROADS = 10.0

    }

    private var location: Location? = null
        set(value) {
            field = value
            if (value != null) {
                RxBus.publish(RxEvent.EventLocation(value))
                LocationPreferences.setLastLocation(this, value)
            }
        }

    private var isGPSEnabled = false
    private var distanceBetweenPoints: Double = 0.0
        set(value) {
            field = value
            RxBus.publish(RxDistance.EventDistance(value))
            LocationPreferences.setDistance(this, value.toFloat())
        }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_map)
            .setContentTitle("GPS")
            .setContentText("GPS tracking")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(false)

        startForeground(1, builder.build())
//        with(NotificationManagerCompat.from(this)) {
//            notify(1, builder.build())
//        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        findLocation()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onLocationChanged(newLocation: Location?) {
        if (location != null) {
            distanceBetweenPoints += distance(location, newLocation)
        }
        location = newLocation
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    private fun findLocation(): Location? {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        try {
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L,
                        10f,
                        this
                    )
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
//                RxBus.publish(RxEvent.EventLocation(location))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can't find location", e)
        }
        return location
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "GPS", importance).apply {
                description = "Tracking for gps"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun distance(point1: Location?, point2: Location?): Double {
        if (point1 == null || point2 == null) {
            return 0.0
        }
        val point1Lat = point1.latitude
        val point1Lon = point1.longitude
        val point2Lat = point2.latitude
        val point2Lon = point2.longitude
        val dLat = Math.toRadians(point2Lat - point1Lat)
        val dLng = Math.toRadians(point2Lon - point1Lon)

        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(Math.toRadians(point1Lat))
                * cos(Math.toRadians(point2Lat)) * sin(dLng / 2) * sin(dLng / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = EARTH_RADIUS * c
        return distance * METER_CONVERSATION
    }

}