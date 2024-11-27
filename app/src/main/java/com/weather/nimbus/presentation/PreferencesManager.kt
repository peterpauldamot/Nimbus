/*
 * Created by Peter Paul Damot on 11-28-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-28-2024.
 */

package com.weather.nimbus.presentation

import android.content.Context
import com.weather.nimbus.common.model.AppThemes
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Save selected theme to preferences
    fun saveTheme(theme: AppThemes) {
        sharedPreferences.edit().putString("selected_theme", theme.name).apply()
    }

    // Retrieve the selected theme
    fun getTheme(): AppThemes {
        val themeName = sharedPreferences.getString("selected_theme", AppThemes.DYNAMIC.name)
        return AppThemes.valueOf(themeName ?: AppThemes.DYNAMIC.name)
    }
}