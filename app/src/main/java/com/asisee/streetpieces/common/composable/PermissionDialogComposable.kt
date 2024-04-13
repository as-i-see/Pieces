package com.asisee.streetpieces.common.composable

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.asisee.streetpieces.common.ext.alertDialog
import com.asisee.streetpieces.common.ext.permissionButton
import com.asisee.streetpieces.theme.BrightOrange
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.asisee.streetpieces.R.string as AppText

@Composable
fun PermissionDialog(@StringRes title: Int, @StringRes text: Int, onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = title)) },
            text = { Text(stringResource(id = text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false
                    },
                    modifier = Modifier.permissionButton(),
                    colors =
                        androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = BrightOrange, contentColor = Color.White)) {
                        Text(text = stringResource(AppText.request_notification_permission))
                    }
            },
            onDismissRequest = {})
    }
}

@Composable
fun RationaleDialog(@StringRes title: Int, @StringRes text: Int) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(id = title)) },
            text = { Text(stringResource(id = text)) },
            confirmButton = {
                TextButton(
                    onClick = { showWarningDialog = false },
                    modifier = Modifier.permissionButton(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = BrightOrange, contentColor = Color.White)) {
                        Text(text = stringResource(AppText.ok))
                    }
            },
            onDismissRequest = { showWarningDialog = false })
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    @StringRes rationale: Int,
    permissions: List<String>
) {
    val permissionState = rememberMultiplePermissionsState(permissions)

    if (!permissionState.allPermissionsGranted) {
        if (permissionState.shouldShowRationale) RationaleDialog(title, rationale)
        else PermissionDialog(title, text) { permissionState.launchMultiplePermissionRequest() }
    }
}
