package com.galvan.ubicationtest.Repository

import android.Manifest
import android.R.attr.priority
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationRequest
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.adedeveloment.slotmachine.manager.ManagerData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow: StateFlow<Location?> = _locationFlow

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates() {
        fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { result->
            _locationFlow.value = result
        }.addOnFailureListener { expetion ->
            Log.e("Error de ubicacion","Error de Ubicacion",expetion)
        } .addOnCanceledListener {
            Log.w("Ubicación", "La solicitud de ubicación fue cancelada")
        }
    }

}