package com.omongolefred.weatherapp.domain.useCases

import com.omongolefred.weatherapp.modal.repository.WeatherRepository
import javax.inject.Inject

class GetCityWeatherInfoUseCase @Inject constructor(
    private val repository : WeatherRepository
) {
    suspend operator fun invoke( cityName: String ) = repository.getCityWeather( cityName )
}