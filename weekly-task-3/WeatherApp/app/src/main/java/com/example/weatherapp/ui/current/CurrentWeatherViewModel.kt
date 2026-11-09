package com.example.weatherapp.ui.current

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.NetworkClient
import com.example.weatherapp.R
import com.example.weatherapp.mapWeatherIcon
import com.example.weatherapp.model.LocalCurrentWeather
import kotlinx.coroutines.launch

class CurrentWeatherViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var weather by mutableStateOf(
        LocalCurrentWeather(
            city = "-",
            temperature = 0.0,
            feelsLike = 0.0,
            description = "--",
            humidity = 0,
            windSpeed = 0.0,
            iconResource = R.drawable.weather_unknown
        )
    )
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadWeather()
    }

    fun loadWeather(cityName: String = "Tampere") {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = NetworkClient.weatherApiService.getCurrentWeather(city = cityName, apiKey = BuildConfig.OPEN_WEATHER_API_KEY)
                val weatherDto = response.weather.firstOrNull()
                weather = LocalCurrentWeather(
                    city = response.name ?: cityName,
                    temperature = response.main?.temp ?: 0.0,
                    feelsLike = response.main?.feelsLike ?: 0.0,
                    description = weatherDto?.description?.replaceFirstChar { it.uppercase() } ?: "",
                    humidity = response.main?.humidity ?: 0,
                    windSpeed = response.wind?.speed ?: 0.0,
                    iconResource = mapWeatherIcon(weatherDto?.icon, weatherDto?.main)
                )
            } catch (e: Exception) {
                errorMessage = "Virhe sään lataamisessa: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}