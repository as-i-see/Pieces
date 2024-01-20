package com.asisee.streetpieces.screens.camera.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionBox(
    modifier: Modifier = Modifier,
    onGalleryClick: () -> Unit,
    onTakePicture: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        PictureActions(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 32.dp),
            onGalleryClick = onGalleryClick,
            onTakePicture = onTakePicture,
            onSwitchCamera = onSwitchCamera
        )
    }
}