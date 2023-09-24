package com.omongolefred.weatherapp.util

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "7a3d16a529d2bb20a1d986fda1c03cef"
    val SEARCH_QUERY_KEY = stringPreferencesKey("search_query_key")
}