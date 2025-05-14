package com.galvan.ubicationtest.Activity.MainActivity

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galvan.ubicationtest.Repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    //Repositorio de Ubicación
    private val ubicationRepository: LocationRepository
): ViewModel() {

    //Uso de StateFlow para observar actualizaciones de ubicación en tiempo real desde la Activity
    val locationFlowViewModel: StateFlow<Location?> = ubicationRepository.locationFlow

    init {
        ///Corrutina asincrónica para actualizar la ubicación en tiempo real cada 800 ms
        viewModelScope.launch {
            while (isActive) {
                ubicationRepository.startLocationUpdates()
                delay(800)
            }
        }
    }


}