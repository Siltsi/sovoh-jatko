package com.example.deviceapidemo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen() {
    val context = LocalContext.current

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager}

    val lightSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) }

    var lux by remember {
        mutableFloatStateOf(0f)
    }

    val description = when {
        lux < 50 -> "Dark"
        lux < 500 -> "Comfortable"
        lux < 10000 -> "Bright"
        else -> "Very bright"
    }

    DisposableEffect(lightSensor) {
        if (lightSensor != null) {
            val listener = object : android.hardware.SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        lux = event.values[0]
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }
            }
            sensorManager.registerListener(
                listener,
                lightSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        } else {
            onDispose {  }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Device APIs")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Light sensor",
                style = MaterialTheme.typography.headlineSmall
            )

            if (lightSensor != null) {
                SensorValue(lux, description)
            } else {
                Text(
                    text = "Light sensor not found!"
                )
            }

            HorizontalDivider()

            Text(
                text = "Help",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://en.wikipedia.org/wiki/Lux".toUri()
                    )
                    try {
                        context.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "Browser not found!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("What is lux?")
            }
        }
    }
}

@Composable
private fun SensorValue(
    lux: Float,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${lux.toInt()} lux",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}