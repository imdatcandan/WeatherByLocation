package com.imdatcandan.weatherbylocation.model

data class Currently(
    val time: Long,
    val summary: String,
    val icon: String,
    val temperature: Double
)