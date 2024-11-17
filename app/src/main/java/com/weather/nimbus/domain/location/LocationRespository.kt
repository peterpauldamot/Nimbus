/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.domain.location

import android.location.Location
import com.weather.nimbus.data.location.LocationProvider

class LocationRepository(private val locationProvider: LocationProvider) {
    suspend fun getCurrentLocation(): Location? {
        return locationProvider.getCurrentLocation()
    }
}