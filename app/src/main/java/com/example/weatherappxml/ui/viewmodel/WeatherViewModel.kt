package com.example.weatherappxml.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappxml.data.model.WeatherResponse
import com.example.weatherappxml.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeather(city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.postValue(it)
                    }
                } else {
                    _error.postValue("City not found")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}
