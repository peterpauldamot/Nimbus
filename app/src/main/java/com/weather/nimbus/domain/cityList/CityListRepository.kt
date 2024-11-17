/*
 * Created by Peter Paul Damot on 11-18-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.domain.cityList

import android.content.Context
import com.google.gson.Gson
import com.weather.nimbus.data.cityList.model.CityListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class CityListRepository (private val context: Context) {
    suspend fun loadCityList(): List<CityListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("CityList.json")
                val reader = InputStreamReader(inputStream)
                val gson = Gson()
                val cityList: List<CityListResponse> = gson.fromJson(reader, Array<CityListResponse>::class.java).toList()
                cityList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
