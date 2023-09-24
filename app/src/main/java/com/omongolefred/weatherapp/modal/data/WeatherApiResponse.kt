package com.omongolefred.weatherapp.modal.data

data class WeatherApiResponse(
    val main : WeatherData,
    val sys : Sys,
    val name: String,
    val wind : Wind,
    val weather: List<Weather>
)

data class WeatherData(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int,
)

data class Sys(
    val sunrise: Int,
    val sunset: Int
)

data class Wind(
    val speed: Double
)

data class Weather(
    val main: String,
    val description: String
)


