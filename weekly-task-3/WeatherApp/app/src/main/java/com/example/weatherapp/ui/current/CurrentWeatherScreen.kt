package com.example.weatherapp.ui.current

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.model.LocalCurrentWeather
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(
    onForecastClick: (String) -> Unit,
    viewModel: CurrentWeatherViewModel = viewModel()
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Current Weather") }) }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(viewModel.weather.city, style = MaterialTheme.typography.headlineMedium)
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.errorMessage != null){
                Text(viewModel.errorMessage!!)
                Spacer(modifier = Modifier.weight(1f))
            } else {
                CurrentWeatherCard(viewModel.weather)
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = { viewModel.loadWeather()},
                enabled = !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Load weather")
            }
            Button(
                onClick = {
                    onForecastClick(viewModel.weather.city)
                },
                enabled = !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View forecast")
            }
        }
    }
}

@Composable
fun CurrentWeatherCard(weather: LocalCurrentWeather) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painterResource(weather.iconResource), weather.description, Modifier.size(100.dp))
            Spacer(Modifier.height(8.dp))
            Text("${weather.temperature.roundToInt()} °C", style = MaterialTheme.typography.displayMedium)
            Text(weather.description, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                WeatherValue("Feels like", "${weather.feelsLike.roundToInt()} °C")
                WeatherValue("Humidity", "${weather.humidity} %")
                WeatherValue("Wind", "${weather.windSpeed.roundToInt()} m/s")
            }
        }
    }
}

@Composable
private fun WeatherValue(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
