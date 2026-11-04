package com.example.rest

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "https://jsonplaceholder.typicode.com/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

    val api: TodoApiService =
        retrofit.create(
            TodoApiService::class.java
        )
}