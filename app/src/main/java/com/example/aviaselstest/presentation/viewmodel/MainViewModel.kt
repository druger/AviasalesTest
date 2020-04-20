package com.example.aviaselstest.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aviaselstest.data.remote.RestApi
import com.example.aviaselstest.domain.repository.CityRepository
import com.example.aviaselstest.domain.model.City
import com.example.aviaselstest.presentation.model.Direction
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    private val repository: CityRepository = CityRepository(RestApi.cityApi)

    val citiesLiveData = MutableLiveData<Pair<List<City>?, Direction>>()

    var from: City? = null
    var to: City? = null

    fun getCities(query: String, direction: Direction) {
        viewModelScope.launch {
            val cities = repository.getCities(query)
            citiesLiveData.postValue(cities to direction)
        }
    }
}
