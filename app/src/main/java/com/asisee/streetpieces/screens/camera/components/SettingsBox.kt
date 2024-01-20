package com.asisee.streetpieces.screens.camera.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asisee.streetpieces.common.ext.roundTo
import com.asisee.streetpieces.screens.camera.model.Flash
import kotlinx.coroutines.delay

@Composable
fun SettingsBox(
    modifier: Modifier = Modifier,
    zoomRatio: Float,
    zoomHasChanged: Boolean,
    flashMode: Flash,
    hasFlashUnit: Boolean,
    onFlashModeChanged: (Flash) -> Unit,
    onZoomFinish: () -> Unit,
) {
    Box(modifier = modifier) {
        FlashBox(
            modifier = Modifier.align(Alignment.TopStart),
            hasFlashUnit = hasFlashUnit,
            flashMode = flashMode,
            onFlashModeChanged = onFlashModeChanged
        )
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                modifier = Modifier.padding(top = 16.dp),
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                visible = zoomHasChanged
            ) {
                Text(
                    text = "${zoomRatio.roundTo(1)}X",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
            }
        }
    }
    LaunchedEffect(zoomRatio, zoomHasChanged) {
        delay(1_000)
        onZoomFinish()
    }
}