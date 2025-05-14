package com.galvan.ubicationtest.Aplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Anotación que indica que esta clase es el punto de entrada para Hilt
@HiltAndroidApp
class UbicationTest : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}