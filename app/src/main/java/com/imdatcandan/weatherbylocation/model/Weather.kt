package com.imdatcandan.weatherbylocation.model

data class Weather(
    val latitude: Long,
    val longitude: Long,
    val currently: Currently
)