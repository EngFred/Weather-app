package com.omongolefred.weatherapp.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.omongolefred.weatherapp.R
import com.omongolefred.weatherapp.databinding.ActivityWeatherBinding
import com.omongolefred.weatherapp.modal.data.WeatherApiResponse
import com.omongolefred.weatherapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private var _binding: ActivityWeatherBinding? = null
    private val binding
        get()= checkNotNull( _binding )

    private val vM: WeatherActivityViewModel by viewModels()

    override fun onCreate( savedInstanceState : Bundle? ) {
        super.onCreate( savedInstanceState )
        _binding = ActivityWeatherBinding.inflate( layoutInflater )
        setTheme( R.style.Theme_WeatherApp )
        setContentView( binding.root )

        lifecycleScope.launch {
            repeatOnLifecycle( Lifecycle.State.STARTED ) {
                vM.weatherData.collect{
                    when(it) {
                        Resource.Loading -> {
                            showProgressDialog()
                        }
                        is Resource.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            binding.infoText.text = Html.fromHtml("<b>Something went wrong! Please make sure your connected to the internet. When all you think is good, try restarting the app :)</b>")
                            Toast.makeText(this@WeatherActivity , it.errorMessage , Toast.LENGTH_LONG ).show()
                        }
                        is Resource.Success -> {
                            hideProgressDialog()
                            updateUi( it.resultData )
                        }
                    }
                }
            }
        }

        binding.searchView.setOnQueryTextListener( object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit( query : String? ) : Boolean {
                query?.let {
                    vM.getCityWeather( it.lowercase() )
                    hideKeyBoard()
                }
                return true
            }
            override fun onQueryTextChange(newText : String?) : Boolean {
                return false
            }
        })

    }

    private fun hideProgressDialog() {
        binding.progressBox.visibility=View.GONE
        binding.contentMain.visibility=View.VISIBLE
    }

    private fun showProgressDialog() {
        binding.progressBox.visibility=View.VISIBLE
        binding.contentMain.visibility=View.INVISIBLE
        binding.root.setBackgroundColor(Color.WHITE)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(weatherData : WeatherApiResponse) {
        binding.tvTemperature.text = "${ weatherData.main.temp} °C"
        binding.tvMaxTemperature.text = "Max: ${weatherData.main.temp_max} °C"
        binding.tvMinTemperature.text = "Min: ${weatherData.main.temp_min} °C"
        binding.tvCityName.text = weatherData.name
        binding.tvHumidity.text = "${weatherData.main.humidity} %"
        binding.tvWind.text = "${weatherData.wind.speed} m/s"
        binding.tvSunrise.text = getTime(  weatherData.sys.sunrise.toLong() )
        binding.tvSunset.text = getTime( weatherData.sys.sunset.toLong() )
        binding.tvWeatherType.text = weatherData.weather[0].main
        binding.tvSea.text = "${weatherData.main.sea_level} hPa"
        binding.condition.text = weatherData.weather[0].main
        binding.tvDayOfTheWeek.text = getDayName()
        binding.tvDate.text = getDate()

        when( weatherData.weather[0].main ) {
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain", "Rain" -> {
                binding.root.setBackgroundResource( R.drawable.rain_background )
                binding.lottiAnimationView.setAnimation( R.raw.rain )
                binding.lottiAnimationView.playAnimation()
            }
            "Light Snow", "Blizzard", "Moderate Snow", "Heavy Snow", "Snow" -> {
                binding.root.setBackgroundResource( R.drawable.snow_background )
                binding.lottiAnimationView.setAnimation( R.raw.snow )
                binding.lottiAnimationView.playAnimation()
            }
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource( R.drawable.sunny_background )
                binding.lottiAnimationView.setAnimation( R.raw.sun )
                binding.lottiAnimationView.playAnimation()
            }
            "Partly Clouds", "Clouds", "Overcast", "Mist",  "Foggy" -> {
                binding.root.setBackgroundResource( R.drawable.colud_background )
                binding.lottiAnimationView.setAnimation( R.raw.cloud )
                binding.lottiAnimationView.playAnimation()
            }
            else -> {
                binding.root.setBackgroundResource( R.drawable.sunny_background )
                binding.lottiAnimationView.setAnimation( R.raw.sun )
                binding.lottiAnimationView.playAnimation()
            }
        }
    }

    private fun getDayName() : String {
        val sdf = SimpleDateFormat( "EEEE", Locale.getDefault() )
        return sdf.format(  Date() )
    }

    private fun getDate() : String {
        val sdf = SimpleDateFormat( "dd/MM/yyyy", Locale.getDefault() )
        return sdf.format(  Date() )
    }

    private fun getTime( timeStamp: Long ) : String {
        val sdf = SimpleDateFormat( "hh:mm", Locale.getDefault() )
        return sdf.format(  Date( timeStamp*1000 ) )
    }

    private fun hideKeyBoard() {
        val imm = getSystemService( INPUT_METHOD_SERVICE ) as InputMethodManager
        imm.hideSoftInputFromWindow( currentFocus?.windowToken , 0 )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}


