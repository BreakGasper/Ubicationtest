package com.galvan.ubicationtest.Activity.MainActivity

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import com.galvan.ubicationtest.Repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val ubicationRepository: LocationRepository
): ViewModel() {

    val locationFlowViewModel: StateFlow<Location?> = ubicationRepository.locationFlow

    init {
        ubicationRepository.startLocationUpdates()
    }


}