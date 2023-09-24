package com.omongolefred.weatherapp.util

sealed class Resource<out R> {
    data class Success<out R>( val resultData: R ) : Resource<R>()
    data class Failure( val errorMessage: String ) : Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}