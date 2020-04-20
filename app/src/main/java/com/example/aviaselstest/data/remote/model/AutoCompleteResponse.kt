package com.example.aviaselstest.data.remote.model

import com.google.gson.annotations.SerializedName

data class AutoCompleteResponse(
    @SerializedName("cities") val cities: List<CityModel>
)
