package com.galvan.ubicationtest.Module

import android.content.Context
import com.galvan.ubicationtest.Repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun root(@ApplicationContext context: Context): LocationRepository {
        return LocationRepository(context)
    }


}