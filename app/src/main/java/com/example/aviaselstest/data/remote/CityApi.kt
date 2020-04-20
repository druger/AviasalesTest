package com.example.aviaselstest.data.remote

import com.example.aviaselstest.data.remote.model.AutoCompleteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("autocomplete")
    suspend fun cities(
        @Query("term") query: String,
        @Query("lang") lang: String = "ru"
    ): Response<AutoCompleteResponse>
}