package com.example.taxitestapp

import android.app.Application
import android.content.Intent
import com.example.taxitestapp.service.GpsService

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startService(Intent(this, GpsService::class.java))

    }

}