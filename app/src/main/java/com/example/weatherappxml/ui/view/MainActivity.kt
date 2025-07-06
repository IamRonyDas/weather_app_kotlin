package com.example.weatherappxml

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappxml.adapter.WeatherAdapter
import com.example.weatherappxml.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val editText = findViewById<EditText>(R.id.editTextText)
        val button = findViewById<Button>(R.id.button)
        val recyclerView = findViewById<RecyclerView>(R.id.weatherRecycler)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.weatherData.observe(this) { weather ->
            adapter = WeatherAdapter(
                listOf(
                    WeatherInfo("Max Temp", "${weather.main.temp_max}°C"),
                    WeatherInfo("Min Temp", "${weather.main.temp_min}°C"),
                    WeatherInfo("Pressure", "${weather.main.pressure} hPa"),
                    WeatherInfo("Humidity", "${weather.main.humidity}%")
                )
            )
            recyclerView.adapter = adapter

            findViewById<TextView>(R.id.tvCity).text = weather.name
            findViewById<TextView>(R.id.tvTemp).text = "${weather.main.temp.toInt()}°C"
            findViewById<TextView>(R.id.tvCondition).text = weather.weather[0].main

            // ✅ Set current live date
            val currentDate = SimpleDateFormat("EEE, d MMM yyyy - hh:mm a", Locale.getDefault()).format(Date())
            findViewById<TextView>(R.id.tvDate).text = currentDate
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            val city = editText.text.toString().trim()
            if (city.isNotEmpty()) {
                viewModel.fetchWeather(city)
            } else {
                Toast.makeText(this, "Enter a city", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Properly placed override outside onCreate
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}
