/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.data.location.source.provider

import android.location.Location

interface LocationProvider {
    suspend fun getCurrentLocation(): Location?
}