package com.example.weatherapp.model

data class LocalCurrentWeather(
    val city: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val iconResource: Int
)

data class LocalForecastItem(
    val date: String,
    val minimumTemperature: Double,
    val maximumTemperature: Double,
    val description: String,
    val iconResource: Int
)
