package com.example.aviaselstest.data.remote.model

import com.example.aviaselstest.domain.model.City
import com.example.aviaselstest.domain.model.Location
import com.google.gson.annotations.SerializedName

data class CityModel(
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("location") val location: LocationModel
)

fun CityModel.map() = City(countryCode, fullName, location.map())

data class LocationModel(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

fun LocationModel.map() = Location(lat, lon)