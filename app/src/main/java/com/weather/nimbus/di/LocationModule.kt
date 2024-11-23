/*
 * Created by Peter Paul Damot on 11-23-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.di

import android.content.Context
import com.weather.nimbus.data.location.source.provider.LocationProviderImpl
import com.weather.nimbus.data.location.source.provider.LocationProvider
import com.weather.nimbus.domain.location.GetCurrentLocationUseCase
import com.weather.nimbus.data.location.source.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    fun provideLocationProvider(context: Context): LocationProvider {
        return LocationProviderImpl(context)
    }

    @Provides
    fun provideLocationRepository(locationProvider: LocationProvider): LocationRepository {
        return LocationRepository(locationProvider)
    }

    @Provides
    fun provideGetCurrentLocationUseCase(locationRepository: LocationRepository): GetCurrentLocationUseCase {
        return GetCurrentLocationUseCase(locationRepository)
    }
}