/*
 * Created by Peter Paul Damot on 11-17-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.data.cities.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CitiesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("coord") val coordinates: Coordinates
) {
    @Serializable
    data class Coordinates(
        @SerializedName("lon") val longitude: Double,
        @SerializedName("lat") val latitude: Double
    )
}


