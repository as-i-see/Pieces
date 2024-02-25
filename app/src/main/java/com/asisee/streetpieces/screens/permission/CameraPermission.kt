package com.asisee.streetpieces.screens.permission

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import com.asisee.streetpieces.common.composable.RequestPermissionsDialog
import com.asisee.streetpieces.R.string as AppText

@Composable
fun RequestCameraPermissionDialog() {
    RequestPermissionsDialog(
        title = AppText.camera_permission_title,
        text = AppText.camera_permission_text,
        rationale = AppText.camera_permission_rationale,
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
