package com.yeongil.digitalwellbeing.utils

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FusedLocationClient(private val client: FusedLocationProviderClient) {
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location {
        return suspendCoroutine<Location> { cont ->
            client.lastLocation.addOnSuccessListener {
                cont.resumeWith(Result.success(it))
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }
    }
}