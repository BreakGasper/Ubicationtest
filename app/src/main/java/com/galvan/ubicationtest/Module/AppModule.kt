package com.galvan.ubicationtest.Module

import android.content.Context
import com.galvan.ubicationtest.Repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Define un módulo de Hilt que proporciona dependencias a nivel de aplicación
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provee una instancia única de LocationRepository en toda la app
    @Provides
    @Singleton
    fun root(@ApplicationContext context: Context): LocationRepository {
        return LocationRepository(context)
    }


}