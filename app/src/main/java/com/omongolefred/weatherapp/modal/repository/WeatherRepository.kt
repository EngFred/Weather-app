package com.omongolefred.weatherapp.modal.repository

import com.omongolefred.weatherapp.domain.dataSource.DataSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val dataSource : DataSource
) {
    suspend fun getCityWeather( cityName: String ) = dataSource.getCityWeather( cityName )
}