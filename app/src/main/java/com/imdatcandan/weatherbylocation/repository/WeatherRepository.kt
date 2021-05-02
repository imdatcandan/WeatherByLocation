package com.imdatcandan.weatherbylocation.repository

import com.imdatcandan.weatherbylocation.model.Weather

class WeatherRepository(private val weatherService: WeatherService) {

    suspend fun getWeatherByLocation(latitude: Long, longitude: Long): Weather {
        return weatherService.getWeatherByLocation(latitude, longitude)
    }
}
