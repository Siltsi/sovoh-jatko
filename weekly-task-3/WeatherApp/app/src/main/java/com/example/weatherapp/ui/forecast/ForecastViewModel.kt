package com.example.weatherapp.ui.forecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.NetworkClient
import com.example.weatherapp.mapForecastResponse
import com.example.weatherapp.model.LocalForecastItem
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var city by mutableStateOf("Tampere")
        private set

    var forecast by mutableStateOf<List<LocalForecastItem>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)

    fun loadForecast(cityName: String = city) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = NetworkClient.weatherApiService.getForecast(city = cityName, apiKey = BuildConfig.OPEN_WEATHER_API_KEY)
                city = response.city?.name ?: cityName
                forecast = mapForecastResponse(response)
            } catch (e: Exception) {
                errorMessage = "Virhe ennusteen lataamisessa: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
            isLoading = false
        }
    }
}