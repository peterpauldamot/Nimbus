/*
 * Created by Peter Paul Damot on 11-18-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-18-2024.
 */

package com.weather.nimbus.domain.cityList

import com.weather.nimbus.data.cityList.model.CityListResponse

class CityListUseCase(private val cityListRepository: CityListRepository) {
    suspend fun getCityList(): List<CityListResponse> {
        return cityListRepository.loadCityList()
    }
}