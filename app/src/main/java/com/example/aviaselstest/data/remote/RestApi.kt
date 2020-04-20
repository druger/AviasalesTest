package com.example.aviaselstest.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestApi {

    private const val BASE_URL = "https://yasen.hotellook.com"

    private fun retrofit() =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val cityApi: CityApi = retrofit().create(CityApi::class.java)
}