package com.example.aviaselstest.domain.model

data class City(
    val countryCode: String,
    val fullName: String,
    val location: Location
) {
    override fun toString(): String {
        return fullName
    }
}

data class Location(
    val lat: Double,
    val lon: Double
)