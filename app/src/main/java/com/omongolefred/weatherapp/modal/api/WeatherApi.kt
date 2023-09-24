package com.omongolefred.weatherapp.modal.api

import com.omongolefred.weatherapp.modal.data.WeatherApiResponse
import com.omongolefred.weatherapp.util.Constants.API_KEY
import com.omongolefred.weatherapp.util.Resource
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather/")
    suspend fun getCityWeather(
        @Query("q") cityName: String,
        @Query("appid") appid: String = API_KEY
    ) : WeatherApiResponse

}