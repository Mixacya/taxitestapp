package com.example.taxitestapp.rxbus

import android.location.Location

class RxEvent {
    data class EventLocation(val location: Location?)

    data class EventLocationTurnOn(val isGpsEnagled: Boolean)
}