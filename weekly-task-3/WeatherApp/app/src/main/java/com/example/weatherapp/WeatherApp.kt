package com.example.weatherapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.current.CurrentWeatherScreen
import com.example.weatherapp.ui.forecast.ForecastScreen

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "current") {
        composable("current") {
            CurrentWeatherScreen(onForecastClick = { city -> navController.navigate("forecast/$city") })
        }
        composable("forecast/{city}") {
                backStackEntry -> val city = backStackEntry.arguments?.getString("city") ?: "Tampere"
            ForecastScreen(city = city, onBackClick = { navController.popBackStack() })
        }
    }
}
