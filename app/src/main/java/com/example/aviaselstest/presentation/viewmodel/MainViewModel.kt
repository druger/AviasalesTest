package com.example.aviaselstest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aviaselstest.data.remote.RestApi
import com.example.aviaselstest.domain.model.City
import com.example.aviaselstest.domain.repository.CityRepository
import com.example.aviaselstest.presentation.model.Direction
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository: CityRepository = CityRepository(RestApi.cityApi)

    private val _citiesLiveData = MutableLiveData<Pair<List<City>?, Direction>>()
    val citiesLiveData: LiveData<Pair<List<City>?, Direction>> = _citiesLiveData

    var from: City? = null
    var to: City? = null

    fun getCities(query: String, direction: Direction) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                val cities = repository.getCities(query)
                _citiesLiveData.postValue(cities to direction)
            }
        }
    }
}
