package com.asisee.streetpieces.screens.permission

import android.Manifest
import androidx.compose.runtime.Composable
import com.asisee.streetpieces.common.composable.RequestPermissionsDialog
import com.asisee.streetpieces.R.string as AppText

@Composable
fun RequestLocationPermissionDialog() {
    RequestPermissionsDialog(
        title = AppText.location_permission_title,
        text = AppText.location_permission_text,
        rationale = AppText.location_permission_rationale,
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ))
}
