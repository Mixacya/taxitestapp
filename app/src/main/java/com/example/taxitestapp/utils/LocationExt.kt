package com.example.taxitestapp.utils

import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

fun FragmentActivity.askTurnLocationIfNeeded(requestCode: Int, onShowDialog: (Boolean) -> Unit) {
    val locationRequest = LocationRequest()
    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setNeedBle(true)
    LocationServices.getSettingsClient(this)
        .checkLocationSettings(builder.build())
        .addOnCompleteListener { task ->
            try {
                val result = task.getResult(ApiException::class.java)
                onShowDialog.invoke(false)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvable = e as ResolvableApiException
                            resolvable.startResolutionForResult(this, requestCode)
                            onShowDialog.invoke(true)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    else -> {
                        onShowDialog.invoke(false)
                    }
                }
            }
        }
}