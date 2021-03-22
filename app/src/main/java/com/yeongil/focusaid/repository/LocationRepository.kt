package com.yeongil.focusaid.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.yeongil.focusaid.data.Location
import com.yeongil.focusaid.dataSource.kakaoApi.KakaoApiService
import retrofit2.await
import java.lang.Exception

class LocationRepository(
    private val kakaoApiService: KakaoApiService
) {
    suspend fun getKeywordLocationList(keyword: String, latLng: LatLng?): List<Location>? {
        return try {
            val response =
                kakaoApiService.getKeywordLocations(keyword, latLng?.latitude, latLng?.longitude)
                    .await()
            response.documents.map {
                val address = if (it.roadAddressName != "") it.roadAddressName else it.addressName
                Location(
                    it.id,
                    it.placeName,
                    address,
                    LatLng(it.y, it.x)
                )
            }
        } catch (error: Exception) {
            Log.e("hello", "getKeywordLocationList: $error")
            null
        }
    }

    suspend fun latLngToLocationName(latLng: LatLng): String? {
        return try {
            val response =
                kakaoApiService.latLngToAddress(latLng.latitude, latLng.longitude).await()

            if (response.documents.isEmpty()) {
                "주소 알 수 없음"
            } else {
                val doc = response.documents[0]
                if (doc.roadAddress != null) {
                    if (doc.roadAddress.buildingName.isNotEmpty()) doc.roadAddress.buildingName
                    else doc.roadAddress.addressName
                } else doc.address.addressName
            }
        } catch (error: Exception) {
            Log.e("hello", "latLngToAddress $error")
            null
        }
    }
}