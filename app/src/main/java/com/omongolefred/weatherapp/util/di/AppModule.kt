package com.omongolefred.weatherapp.util.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.omongolefred.weatherapp.modal.api.WeatherApi
import com.omongolefred.weatherapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn( SingletonComponent::class )
class AppModule {

    @Provides
    fun provideRetrofitInstance() :  Retrofit = Retrofit.Builder()
        .baseUrl(  BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .build()

    @Provides
    fun provideWeatherApiInstance( retrofitInstance: Retrofit ) : WeatherApi= retrofitInstance.create( WeatherApi::class.java )

    @Provides
    fun provideDatastoreInstance(  @ApplicationContext context : Context  ) = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }

}