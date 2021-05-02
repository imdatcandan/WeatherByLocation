package com.imdatcandan.weatherbylocation

import android.app.Application
import com.imdatcandan.weatherbylocation.di.appModule
import com.imdatcandan.weatherbylocation.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherByLocationApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherByLocationApp)
            modules(arrayListOf(appModule, networkModule))
        }
    }
}