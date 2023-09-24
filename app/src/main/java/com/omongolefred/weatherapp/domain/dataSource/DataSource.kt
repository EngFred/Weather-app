package com.omongolefred.weatherapp.domain.dataSource

import com.omongolefred.weatherapp.modal.api.WeatherApi
import javax.inject.Inject

class DataSource @Inject constructor(
    private val api : WeatherApi
) {
    suspend fun getCityWeather( cityName: String ) = api.getCityWeather( cityName )
}