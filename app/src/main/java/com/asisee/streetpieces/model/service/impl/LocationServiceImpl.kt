package com.asisee.streetpieces.model.service.impl

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.model.service.LocationService
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.annotation.Single
import javax.inject.Inject
@Single
class LocationServiceImpl(private val context: Context) : LocationService {
    override suspend fun getCurrentLocation(): Result<Location, LocationResultException> =
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) Err(LocationResultException.PermissionRequestFlow)
        else {
            val location = withTimeoutOrNull(10000) {
                LocationServices.getFusedLocationProviderClient(context)
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .await()
            }
            if (location == null)
                Err(LocationResultException.LocationFetchException)
            else Ok(location)
        }
}
