package com.omongolefred.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omongolefred.weatherapp.domain.useCases.GetCityWeatherInfoUseCase
import com.omongolefred.weatherapp.modal.data.WeatherApiResponse
import com.omongolefred.weatherapp.modal.repository.PreferenceRepository
import com.omongolefred.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class WeatherActivityViewModel @Inject constructor(
    private val getCityWeatherInfoUseCase : GetCityWeatherInfoUseCase,
    private val prefRepository : PreferenceRepository
) : ViewModel() {
    private val _weatherData = MutableStateFlow<Resource<WeatherApiResponse>>( Resource.Loading )
    val weatherData = _weatherData.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {

        viewModelScope.launch {
            prefRepository.storedCityName.collectLatest{
                getCityWeather( it )
            }
        }

    }

    fun getCityWeather( cityName: String ) {
        Resource.Loading
        viewModelScope.launch( Dispatchers.IO + errorHandler ) {
           try {
               prefRepository.storeCityName( cityName )
               val result = getCityWeatherInfoUseCase.invoke( cityName )
               _weatherData.value = Resource.Success( result )
           } catch ( ex: Exception ) {
               when( ex ) {
                   is ConnectException -> _weatherData.value = Resource.Failure("No internet connection!")
                   is HttpException -> _weatherData.value = Resource.Failure("Invalid city name!")
                   else -> Log.d("OMONGOLE", "Error: $ex")
               }
           }
        }
    }
}