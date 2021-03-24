package com.yeongil.focusaid.utils

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FusedLocationClient(private val client: FusedLocationProviderClient) {
    /* This must be called only when the permission is granted.
    * This function queries last location and return it. */
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location? {
        return suspendCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener {
                    if (it == null) cont.resumeWith(Result.success(null))
                    else cont.resumeWith(Result.success(it))
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }
}