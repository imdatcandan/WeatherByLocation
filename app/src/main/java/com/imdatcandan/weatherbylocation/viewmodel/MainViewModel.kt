package com.imdatcandan.weatherbylocation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdatcandan.weatherbylocation.model.Weather
import com.imdatcandan.weatherbylocation.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    val weatherInfo: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getWeatherByLocation(latitude: Long, longitude: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weather = weatherRepository.getWeatherByLocation(latitude, longitude)
                weatherInfo.postValue(setWeatherInfo(weather))
            } catch (exception: Exception) {
                error.postValue(exception)
            } finally {
                loading.postValue(false)
            }
        }
    }

    private fun setWeatherInfo(weather: Weather): String {
        val currentWeather = weather.currently
        val weatherInfo = StringBuilder().appendLine(currentWeather.summary)
            .appendLine(currentWeather.temperature)
            .appendLine(convertLongToTime(currentWeather.time))
        return weatherInfo.toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToTime(time: Long): String {
        val date = Date(time * 1000)
        val format = SimpleDateFormat("HH:mm dd.MM.yyyy")
        return format.format(date)
    }
}