package com.asisee.streetpieces.common.composable

import android.Manifest
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asisee.streetpieces.common.ext.alertDialog
import com.asisee.streetpieces.common.ext.permissionButton
import com.asisee.streetpieces.common.ext.textButton
import com.asisee.streetpieces.theme.BrightOrange
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.security.Permissions
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
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BrightOrange,
                        contentColor = Color.White
                    )
                ) { Text(text = stringResource(AppText.request_notification_permission)) }
            },
            onDismissRequest = { }
        )
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
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BrightOrange,
                        contentColor = Color.White
                    )
                ) { Text(text = stringResource(AppText.ok)) }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsDialog(@StringRes title: Int, @StringRes text: Int, @StringRes rationale: Int, permissions: List<String>) {
    val permissionState = rememberMultiplePermissionsState(permissions)

    if (!permissionState.allPermissionsGranted) {
        if (permissionState.shouldShowRationale) RationaleDialog(title, rationale)
        else PermissionDialog(title, text) { permissionState.launchMultiplePermissionRequest() }
    }
}