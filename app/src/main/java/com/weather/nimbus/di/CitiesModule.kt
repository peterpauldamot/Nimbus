/*
 * Created by Peter Paul Damot on 11-23-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.di

import android.content.Context
import com.weather.nimbus.data.cities.source.CitiesRepository
import com.weather.nimbus.domain.cities.GetCitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CitiesModule {
    @Provides
    fun provideCitiesRepository(context: Context): CitiesRepository {
        return CitiesRepository(context)
    }

    @Provides
    fun provideGetCitiesUseCase(citiesRepository: CitiesRepository): GetCitiesUseCase {
        return GetCitiesUseCase(citiesRepository)
    }
}