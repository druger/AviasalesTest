package com.example.aviaselstest.domain.repository

import com.example.aviaselstest.data.remote.CityApi
import com.example.aviaselstest.data.remote.model.map
import com.example.aviaselstest.domain.model.City

class CityRepository(private val api: CityApi): BaseRepository() {

    suspend fun getCities(query: String): List<City>? {
        val cityResponse = apiCall(
            call = { api.cities(query) },
            error = "Error getting cities"
        )
        return cityResponse?.cities?.map { it.map() }
    }
}