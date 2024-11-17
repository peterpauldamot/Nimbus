/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-17-2024.
 */

package com.weather.nimbus.data.weather.model.cities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityListModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("state") val state: String,
    @SerialName("country") val country: String,
    @SerialName("coord") val coordinates: Coordinates
) {
    @Serializable
    data class Coordinates(
        @SerialName("lon") val longitude: String,
        @SerialName("lat") val latitude: String
    )
}


