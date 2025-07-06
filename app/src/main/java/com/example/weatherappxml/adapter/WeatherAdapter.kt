package com.example.weatherappxml.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappxml.R
import com.example.weatherappxml.WeatherInfo

class WeatherAdapter(private val weatherList: List<WeatherInfo>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView = itemView.findViewById(R.id.tvLabel)
        val valueTextView: TextView = itemView.findViewById(R.id.tvValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_card, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherItem = weatherList[position]
        holder.labelTextView.text = weatherItem.label
        holder.valueTextView.text = weatherItem.value
    }

    override fun getItemCount(): Int = weatherList.size
}
