package com.example.weatherappxml.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherappxml.data.model.Main
import com.example.weatherappxml.data.model.Weather
import com.example.weatherappxml.data.model.WeatherResponse
import com.example.weatherappxml.repository.WeatherRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockRepository: WeatherRepository

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeather - success`() = runTest {
        val weatherResponse = WeatherResponse(
            name = "Kolkata",
            main = Main(32.0, 28.0, 35.0, 1012, 60),
            weather = listOf(Weather("Sunny", "Clear sky"))
        )

        `when`(mockRepository.getWeather("Kolkata")).thenReturn(Response.success(weatherResponse))

        viewModel.fetchWeather("Kolkata")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Kolkata", viewModel.weatherData.value?.name)
        assertEquals(1012, viewModel.weatherData.value?.main?.pressure)
    }

    @Test
    fun `fetchWeather - city not found`() = runTest {
        val errorResponse = Response.error<WeatherResponse>(404, ResponseBody.create(null, ""))
        `when`(mockRepository.getWeather("InvalidCity")).thenReturn(errorResponse)

        viewModel.fetchWeather("InvalidCity")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("City not found", viewModel.error.value)
    }
}
