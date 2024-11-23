/*
 * Created by Peter Paul Damot on 11-18-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.domain.cities

import com.weather.nimbus.data.cities.model.CitiesResponse
import com.weather.nimbus.data.cities.source.CitiesRepository

class GetCitiesUseCase(private val citiesRepository: CitiesRepository) {
    suspend operator fun invoke(): List<CitiesResponse> {
        return citiesRepository.getCities()
    }
}