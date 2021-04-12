package com.yeongil.focusaid.dataSource.focusAidApi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

object FocusAidApi {
    private var serviceInstance: FocusAidService? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:3000")
        .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
        .build()

    val service
        get() = serviceInstance ?: retrofit.create(FocusAidService::class.java)
            .also { serviceInstance = it }
}