package com.asisee.streetpieces.screens.camera

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.asisee.streetpieces.common.ext.noClickable
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.camera.components.ActionBox
import com.asisee.streetpieces.screens.camera.components.SettingsBox
import com.asisee.streetpieces.screens.camera.mapper.toFlash
import com.asisee.streetpieces.screens.camera.mapper.toFlashMode
import com.asisee.streetpieces.screens.camera.model.Flash
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.skydoves.cloudy.Cloudy
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.rememberCamSelector
import com.ujizin.camposer.state.rememberCameraState
import com.ujizin.camposer.state.rememberFlashMode
import com.ujizin.camposer.state.rememberTorch
import org.orbitmvi.orbit.compose.collectSideEffect
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.CenteredColumn
import com.asisee.streetpieces.common.composable.SpacerXL
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.asisee.streetpieces.screens.create_piece.CreatePieceScreen
import org.orbitmvi.orbit.compose.collectAsState
import com.asisee.streetpieces.R.string as AppText
class CameraScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<CameraScreenModel>()
//        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is CameraScreenSideEffect.PhotoResult -> {
                    navigator.push(CreatePieceScreen(sideEffect.photoUri))
                }
                is CameraScreenSideEffect.Error -> {
                    SnackbarManager.showMessage(sideEffect.message.toSnackbarMessage())
                }
            }
        }
        View(
            takePicture = screenModel::takePicture,
            openGallery = screenModel::openGallery
        )

    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun View(
        takePicture: (CameraState) -> Unit,
        openGallery: () -> Unit
    ) {
        val cameraState = rememberCameraState()
        val cameraPermissionState = rememberMultiplePermissionsState(
            buildList {
                add(Manifest.permission.CAMERA)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                    add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        )
        if (cameraPermissionState.allPermissionsGranted) {
            CameraSection(
                cameraState = cameraState,
                onTakePicture = { takePicture(cameraState) },
                onGalleryClick = openGallery,
            )
        } else CenteredColumn {
            Text(text = stringResource(id = AppText.camera_permission_text))
            SpacerXL()
            Button(
                onClick = {
                    cameraPermissionState.launchMultiplePermissionRequest()
                }
            ) {
                Text(stringResource(id = AppText.grant))
            }
        }
    }
}

@Composable
fun CameraSection(
    cameraState: CameraState,
    onTakePicture: () -> Unit,
    onGalleryClick: () -> Unit,
) {
    var flashMode by cameraState.rememberFlashMode()
    var camSelector by rememberCamSelector(CamSelector.Back)
    var zoomRatio by rememberSaveable { mutableFloatStateOf(cameraState.minZoom) }
    var zoomHasChanged by rememberSaveable { mutableStateOf(false) }
    val hasFlashUnit by rememberUpdatedState(cameraState.hasFlashUnit)
    var enableTorch by cameraState.rememberTorch(initialTorch = false)
    CameraPreview(
        cameraState = cameraState,
        camSelector = camSelector,
        enableTorch = enableTorch,
        flashMode = flashMode,
        zoomRatio = zoomRatio,
        onZoomRatioChanged = {
            zoomHasChanged = true
            zoomRatio = it
        },
        onSwitchToFront = { bitmap ->
            Cloudy(radius = 20) { Image(bitmap.asImageBitmap(), contentDescription = null) }
        },
        onSwitchToBack = { bitmap ->
            Cloudy(radius = 20) { Image(bitmap.asImageBitmap(), contentDescription = null) }
        }) {
            CameraInnerContent(
                Modifier.fillMaxSize(),
                zoomHasChanged = zoomHasChanged,
                zoomRatio = zoomRatio,
                flashMode = flashMode.toFlash(enableTorch),
                hasFlashUnit = hasFlashUnit,
                onGalleryClick = onGalleryClick,
                onFlashModeChanged = { flash ->
                    enableTorch = flash == Flash.Always
                    flashMode = flash.toFlashMode()
                },
                onZoomFinish = { zoomHasChanged = false },
                onTakePicture = onTakePicture) {
                    if (cameraState.isStreaming) {
                        camSelector = camSelector.inverse
                    }
                }
        }
}

@Composable
fun CameraInnerContent(
    modifier: Modifier = Modifier,
    zoomHasChanged: Boolean,
    zoomRatio: Float,
    flashMode: Flash,
    hasFlashUnit: Boolean,
    onGalleryClick: () -> Unit,
    onFlashModeChanged: (Flash) -> Unit,
    onZoomFinish: () -> Unit,
    onTakePicture: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SettingsBox(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 24.dp, end = 24.dp),
            zoomRatio = zoomRatio,
            zoomHasChanged = zoomHasChanged,
            flashMode = flashMode,
            hasFlashUnit = hasFlashUnit,
            onFlashModeChanged = onFlashModeChanged,
            onZoomFinish = onZoomFinish,
        )
        ActionBox(
            modifier = Modifier
                .fillMaxWidth()
                .noClickable()
                .padding(bottom = 32.dp, top = 16.dp),
            onGalleryClick = onGalleryClick,
            onTakePicture = onTakePicture,
            onSwitchCamera = onSwitchCamera,
        )
    }
}
