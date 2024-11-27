/*
 * Created by Peter Paul Damot on 11-28-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-28-2024.
 */

package com.weather.nimbus.presentation

import android.content.Context
import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.common.model.TemperatureUnit
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    fun saveTheme(theme: AppThemes) {
        sharedPreferences.edit().putString("nimbus_theme", theme.name).apply()
    }

    fun saveTemperatureUnit(unit: TemperatureUnit) {
        sharedPreferences.edit().putString("nimbus_units_temperature", unit.name).apply()
    }

    fun getTheme(): AppThemes {
        val themeName = sharedPreferences.getString("nimbus_theme", AppThemes.DYNAMIC.name)
        return AppThemes.valueOf(themeName ?: AppThemes.DYNAMIC.name)
    }

    fun getTemperatureUnit(): TemperatureUnit {
        val temperatureUnitName = sharedPreferences.getString(
            "nimbus_units_temperature", TemperatureUnit.KELVIN.name)
        return TemperatureUnit.valueOf(temperatureUnitName ?: TemperatureUnit.KELVIN.name)
    }
}