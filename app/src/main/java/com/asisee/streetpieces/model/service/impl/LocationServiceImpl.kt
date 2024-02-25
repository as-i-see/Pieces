package com.asisee.streetpieces.model.service.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.asisee.streetpieces.model.service.LocationService
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(@ApplicationContext private val context: Context) :
    LocationService {
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location =
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()
}
