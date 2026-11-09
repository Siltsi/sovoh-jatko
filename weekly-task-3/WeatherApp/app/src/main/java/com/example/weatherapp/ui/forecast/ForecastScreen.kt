package com.example.weatherapp.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.model.LocalForecastItem
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    city: String,
    onBackClick: () -> Unit,
    viewModel: ForecastViewModel = viewModel()
) {
    LaunchedEffect(city) {
        viewModel.loadForecast(city)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Forecast") },
                navigationIcon = { IconButton(onClick = onBackClick) { Text("←") } }
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            Text(city, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.errorMessage != null) {
                Text(viewModel.errorMessage!!)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.forecast, key = { it.date }) { item -> ForecastCard(item) }
                }
            }
        }
    }
}

@Composable
private fun ForecastCard(item: LocalForecastItem) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(item.iconResource), item.description, Modifier.size(56.dp))
            Column(Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(item.date, style = MaterialTheme.typography.titleMedium)
                Text(item.description)
            }
            Text("${item.minimumTemperature.roundToInt()}° / ${item.maximumTemperature.roundToInt()}°", style = MaterialTheme.typography.titleMedium)
        }
    }
}
