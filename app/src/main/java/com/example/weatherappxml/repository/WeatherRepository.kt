package com.example.weatherappxml.repository

import com.example.weatherappxml.data.network.RetrofitInstance
import com.example.weatherappxml.util.Constants

class WeatherRepository {
    open suspend fun getWeather(city: String) =
        RetrofitInstance.api.getWeatherByCity(city, Constants.WEATHER_API_KEY)
}
