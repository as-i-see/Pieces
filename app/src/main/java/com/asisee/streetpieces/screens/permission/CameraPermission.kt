package com.asisee.streetpieces.screens.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.PermissionDialog
import com.asisee.streetpieces.common.composable.RationaleDialog
import com.asisee.streetpieces.common.composable.RequestPermissionsDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.asisee.streetpieces.R.string as AppText

@Composable
fun RequestCameraPermissionDialog() {
    RequestPermissionsDialog(title = AppText.camera_permission_title, text = AppText.camera_permission_text, rationale = AppText.camera_permission_rationale,
        buildList {
        add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
            add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    })
}