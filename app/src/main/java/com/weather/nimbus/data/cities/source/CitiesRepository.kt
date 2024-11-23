/*
 * Created by Peter Paul Damot on 11-18-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.data.cities.source

import android.content.Context
import com.google.gson.Gson
import com.weather.nimbus.data.cities.model.CitiesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class CitiesRepository (private val context: Context) {
    suspend fun getCities(): List<CitiesResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("CityList.json")
                val reader = InputStreamReader(inputStream)
                val gson = Gson()
                val cityList: List<CitiesResponse> = gson.fromJson(reader, Array<CitiesResponse>::class.java).toList()
                cityList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
