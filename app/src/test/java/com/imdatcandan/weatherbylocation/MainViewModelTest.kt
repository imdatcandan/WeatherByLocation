package com.imdatcandan.weatherbylocation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.imdatcandan.weatherbylocation.model.Weather
import com.imdatcandan.weatherbylocation.repository.WeatherRepository
import com.imdatcandan.weatherbylocation.viewmodel.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val weatherRepository: WeatherRepository = mockk(relaxed = true)
    private val weather: Weather = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = MainViewModel(weatherRepository)
    }

    @Test
    fun testSuccessViewState() = runBlockingTest {
        coEvery {
            weatherRepository.getWeatherByLocation(LATITUDE, LONGITUDE)
        } returns weather

        viewModel.getWeatherByLocation(LATITUDE, LONGITUDE)

        assertEquals(viewModel.loading.getOrAwaitValue(), true)
        assertEquals(viewModel.weatherInfo.getOrAwaitValue(), WEATHER_INFO)
        assertEquals(viewModel.loading.getOrAwaitValue(), false)

    }

    @Test
    fun testErrorViewState() = runBlockingTest {
        coEvery {
            weatherRepository.getWeatherByLocation(LATITUDE, LONGITUDE)
        } throws ERROR

        viewModel.getWeatherByLocation(LATITUDE, LONGITUDE)

        assertEquals(viewModel.loading.getOrAwaitValue(), true)
        assertEquals(viewModel.error.getOrAwaitValue(), ERROR)
        assertEquals(viewModel.loading.getOrAwaitValue(), false)

    }

    private companion object {
        private const val LATITUDE = 59337239L
        private const val LONGITUDE = 18062381L
        private val ERROR = Exception("dummy error")
        private const val WEATHER_INFO = "\n" +
                "0.0\n" +
                "01:00 01.01.1970\n"

    }
}