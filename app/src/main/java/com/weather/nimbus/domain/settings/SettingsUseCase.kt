/*
 * Created by Peter Paul Damot on 11-28-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-28-2024.
 */

package com.weather.nimbus.domain.settings

import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.common.model.TemperatureUnit
import com.weather.nimbus.presentation.PreferencesManager
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    fun getAppTheme(): AppThemes {
        return preferencesManager.getTheme()
    }

    fun getTemperatureUnits(): TemperatureUnit {
        return preferencesManager.getTemperatureUnit()
    }

    fun updateAppTheme(appThemes: AppThemes) {
        preferencesManager.saveTheme(appThemes)
    }

    fun updateTemperatureUnits(temperatureUnits: TemperatureUnit) {
        preferencesManager.saveTemperatureUnit(temperatureUnits)
    }
}