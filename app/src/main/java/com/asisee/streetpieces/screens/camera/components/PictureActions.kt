package com.asisee.streetpieces.screens.camera.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrowseGallery
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.asisee.streetpieces.R

@Composable
fun PictureActions(
    modifier: Modifier = Modifier,
    onGalleryClick: () -> Unit,
    onTakePicture: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
            GalleryButton(onClick = onGalleryClick)
            PictureButton(onClick = onTakePicture)
            SwitchButton(onClick = onSwitchCamera)
        }
}

@Composable
fun GalleryButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier =
            Modifier.size(48.dp)
                .background(Color.DarkGray.copy(alpha = 0.25F), CircleShape)
                .clip(CircleShape)
                .then(modifier),
        onClick = onClick) {
            Icon(
                Icons.Rounded.BrowseGallery,
                contentDescription = stringResource(R.string.gallery),
                tint = Color.White,
                modifier = Modifier.size(24.dp))
        }
}

@Composable
private fun SwitchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var clicked by remember { mutableStateOf(false) }
    val rotate by
        animateFloatAsState(
            targetValue = if (clicked) 360F else 1F, animationSpec = tween(durationMillis = 500))
    Button(
        modifier =
            Modifier.rotate(rotate)
                .size(48.dp)
                .background(Color.DarkGray.copy(alpha = 0.25F), CircleShape)
                .clip(CircleShape)
                .then(modifier),
        onClick = {
            clicked = !clicked
            onClick()
        }) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.refresh),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = stringResource(R.string.refresh))
        }
}

@Composable
private fun PictureButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val color by
        animateColorAsState(
            targetValue = Color.Transparent, animationSpec = tween(durationMillis = 250))

    val innerPadding by animateDpAsState(targetValue = 8.dp)
    val percentShape by animateIntAsState(targetValue = 50)

    Button(
        modifier =
            Modifier.size(80.dp)
                .border(BorderStroke(4.dp, Color.White), CircleShape)
                .padding(innerPadding)
                .background(color, RoundedCornerShape(percentShape))
                .clip(CircleShape)
                .then(modifier),
        onClick = onClick)
}
