package com.yeongil.digitalwellbeing.dataSource.kakaoApi

import com.yeongil.digitalwellbeing.dataSource.kakaoApi.responseData.KeywordSearchResult
import com.yeongil.digitalwellbeing.dataSource.kakaoApi.responseData.LatLngToAddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApiService {
    @GET("v2/local/search/keyword.json")
    fun getKeywordLocations(
        @Query("query") keyword: String,
        @Query("y") lat: Double?,
        @Query("x") long: Double?,
        @Query("sort") sort: String = "distance",
    ): Call<KeywordSearchResult>

    @GET("/v2/local/geo/coord2address.json")
    fun latLngToAddress(
        @Query("y") lat: Double,
        @Query("x") long: Double
    ): Call<LatLngToAddressResponse>
}