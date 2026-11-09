package com.example.weatherapp

import com.example.weatherapp.model.LocalForecastItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Serializable
data class WeatherApiResponse(
    val name: String? = null,
    val main: MainDto? = null,
    val weather: List<WeatherDto> = emptyList(),
    val wind: WindDto? = null
)

@Serializable
data class ForecastApiResponse(
    val city: CityDto? = null,
    val list: List<ForecastItemDto> = emptyList()
)

@Serializable
data class CityDto(
    val name: String? = null
)

@Serializable
data class MainDto(
    val temp: Double? = null,
    @SerialName("feels_like") val feelsLike: Double? = null,
    @SerialName("temp_min") val tempMin: Double? = null,
    @SerialName("temp_max") val tempMax: Double? = null,
    val humidity: Int? = null
)

@Serializable
data class WeatherDto(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)

@Serializable
data class WindDto(
    val speed: Double? = null
)

@Serializable
data class ForecastItemDto(
    val dt: Long? = null,
    @SerialName("dt_txt") val dateText: String? = null,
    val main: MainDto? = null,
    val weather: List<WeatherDto> = emptyList()
)

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "fi"
    ): WeatherApiResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "fi"
    ): ForecastApiResponse

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}

object NetworkClient {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(WeatherApiService::class.java)
    }
}

fun mapWeatherIcon(iconCode: String?, main: String?): Int {
    return when {
        iconCode?.startsWith("01") == true -> R.drawable.weather_clear
        iconCode?.startsWith("02") == true -> R.drawable.weather_partly_cloudy
        iconCode?.startsWith("03") == true || iconCode?.startsWith("04") == true -> R.drawable.weather_cloudy
        iconCode?.startsWith("09") == true || iconCode?.startsWith("10") == true || iconCode?.startsWith("11") == true -> R.drawable.weather_rain
        iconCode?.startsWith("13") == true -> R.drawable.weather_snow
        main.equals("Clear", ignoreCase = true) -> R.drawable.weather_clear
        main.equals("Clouds", ignoreCase = true) -> R.drawable.weather_cloudy
        main.equals("Rain", ignoreCase = true) || main.equals("Drizzle", ignoreCase = true) -> R.drawable.weather_rain
        main.equals("Snow", ignoreCase = true) -> R.drawable.weather_snow
        else -> R.drawable.weather_unknown
    }
}

fun mapForecastResponse(response: ForecastApiResponse): List<LocalForecastItem> {
    val grouped = response.list.groupBy { item ->
        item.dateText?.substringBefore(" ") ?: ""
    }
    return grouped.entries.take(5).map { (dateStr, items) ->
        val minTemp = items.minOfOrNull { it.main?.tempMin ?: (it.main?.temp ?: 0.0) } ?: 0.0
        val maxTemp = items.maxOfOrNull { it.main?.tempMax ?: (it.main?.temp ?: 0.0) } ?: 0.0
        val representativeItem = items.firstOrNull { it.dateText?.contains("12:00") == true } ?: items.first()
        val desc = representativeItem.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: ""
        val icon = mapWeatherIcon(
            representativeItem.weather.firstOrNull()?.icon,
            representativeItem.weather.firstOrNull()?.main
        )
        val day = try {
            val date = LocalDate.parse(dateStr)
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("fi"))
            "$dayName ${date.dayOfMonth}.${date.monthValue}."
                .replaceFirstChar { it.uppercase() }
        } catch (e: Exception) {
            dateStr
        }
        LocalForecastItem(
            date = day,
            minimumTemperature = minTemp,
            maximumTemperature = maxTemp,
            description = desc,
            iconResource = icon
        )
    }
}
