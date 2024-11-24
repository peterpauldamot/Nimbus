/*
 * Created by Peter Paul Damot on 11-23-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-23-2024.
 */

package com.weather.nimbus.di

import com.weather.nimbus.data.weather.source.api.OpenWeatherAPI
import com.weather.nimbus.data.weather.source.repository.OpenWeatherRepository
import com.weather.nimbus.data.weather.source.repository.OpenWeatherRepositoryImpl
import com.weather.nimbus.data.weather.source.transformer.CurrentWeatherResponseTransformer
import com.weather.nimbus.data.weather.source.transformer.FiveDayForecastResponseTransformer
import com.weather.nimbus.domain.weather.GetCurrentWeatherUseCase
import com.weather.nimbus.domain.weather.GetFiveDayForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    @Singleton
    fun provideOpenWeatherRepository(
        openWeatherAPI: OpenWeatherAPI,
        currentWeatherTransformer: CurrentWeatherResponseTransformer,
        fiveDayForecastResponseTransformer: FiveDayForecastResponseTransformer
    ): OpenWeatherRepository {
        return OpenWeatherRepositoryImpl(
            openWeatherAPI,
            currentWeatherTransformer,
            fiveDayForecastResponseTransformer)
    }

    @Provides
    @Singleton
    fun provideGetCurrentWeatherUseCase(
        weatherRepository: OpenWeatherRepository
    ): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(weatherRepository)
    }

    @Provides
    @Singleton
    fun provideGetFiveDayForecastUseCase(
        weatherRepository: OpenWeatherRepository
    ): GetFiveDayForecastUseCase {
        return GetFiveDayForecastUseCase(weatherRepository)
    }

    @Provides
    @Singleton
    fun provideCurrentWeatherResponseTransformer(): CurrentWeatherResponseTransformer {
        return CurrentWeatherResponseTransformer()
    }

    @Provides
    @Singleton
    fun provideFiveDayForecastResponseTransformer(): FiveDayForecastResponseTransformer {
        return FiveDayForecastResponseTransformer()
    }
}