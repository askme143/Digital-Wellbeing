package com.yeongil.focusaid.dataSource.kakaoApi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object KakaoApi {
    private const val apiKey = "3c452cdffef6d0a0873e0b826b22b353"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(Json.asConverterFactory(MediaType.parse("application/json")!!))
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest =
                        chain.request().newBuilder()
                            .addHeader("Authorization", "KakaoAK $apiKey")
                            .build()
                    chain.proceed(newRequest)
                }
                .build()
        )
        .build()

    private var serviceInstance: KakaoApiService? = null
    val service: KakaoApiService
        get() = serviceInstance
            ?: retrofit.create(KakaoApiService::class.java).also { serviceInstance = it }
}