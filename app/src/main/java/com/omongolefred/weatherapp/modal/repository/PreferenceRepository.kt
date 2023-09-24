package com.omongolefred.weatherapp.modal.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.omongolefred.weatherapp.util.Constants.SEARCH_QUERY_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepository @Inject constructor (
    private val dataStore: DataStore<Preferences>
) {
    val storedCityName: Flow<String> = dataStore.data.map {
        it[SEARCH_QUERY_KEY] ?: "Kampala"
    }.distinctUntilChanged()

    suspend fun storeCityName( cityName: String ) {
        dataStore.edit {
                it[SEARCH_QUERY_KEY] = cityName
        }
    }

}