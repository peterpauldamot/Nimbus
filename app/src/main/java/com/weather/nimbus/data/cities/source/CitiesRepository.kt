/*
 * Created by Peter Paul Damot on 11-18-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.data.cities.source

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.weather.nimbus.data.cities.model.CitiesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class CitiesRepository (private val context: Context) {
    companion object {
        const val CITIES_FILENAME = "CityList.json"
    }

    suspend fun getCities(): Result<List<CitiesResponse>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                Log.d("CitiesRepository", "Fetching cities from file: $CITIES_FILENAME")
                val inputStream = context.assets.open(CITIES_FILENAME)
                val reader = JsonReader(InputStreamReader(inputStream))
                val gson = Gson()

                val cityListType = object : TypeToken<List<CitiesResponse>>() {}.type
                val cities: List<CitiesResponse> = gson.fromJson(reader, cityListType)
                Log.d("CitiesRepository", "Success fetching cities: $cities")
                cities
            }.onFailure { exception ->
                exception.printStackTrace()
                Log.e("CitiesRepository", "Error fetching cities", exception)
            }
        }
    }
}
