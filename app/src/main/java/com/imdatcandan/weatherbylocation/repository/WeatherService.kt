package com.imdatcandan.weatherbylocation.repository

import com.imdatcandan.weatherbylocation.model.Weather
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("forecast/2bb07c3bece89caf533ac9a5d23d8417/{latitude},{longitude}")
    suspend fun getWeatherByLocation(
        @Path("latitude") latitude: Long,
        @Path("longitude") longitude: Long
    ): Weather
}