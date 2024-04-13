package com.asisee.streetpieces.screens.create_piece

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import androidx.compose.material3.CircularProgressIndicator
import com.asisee.streetpieces.common.composable.ActionToolbar
import com.asisee.streetpieces.common.composable.BasicField
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.common.ext.circularProgressIndicator
import com.asisee.streetpieces.common.ext.row
import com.asisee.streetpieces.model.PieceLocation
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import com.asisee.streetpieces.R.string as AppText

@Composable
fun CreatePieceView(
    state: CreatePieceState,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onFetchLocationClick: () -> Unit,
    onRequestLocationPermissionClick: () -> Unit,
    locationPermissionGranted: Boolean,
) {
    if (state.pieceIsUploading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        )
        {
            ActionToolbar(
                title = R.string.edit_piece,
                endActionIcon = R.drawable.ic_check,
                modifier = Modifier) {
                onDoneClick() }
            SpacerM()
            Row(
                Modifier.row(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            )
            {
                BasicField(
                    R.string.title,
                    state.piece.title,
                    { newValue -> onTitleChange(newValue) },
                    Modifier.fillMaxWidth(0.7f))
                AsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(state.piece.photoUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.preview),
                    placeholder = painterResource(id = R.drawable.photo_placeholder),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(RectangleShape)
                        .width(100.dp)
                        .height(100.dp),
                )
            }
            SpacerS()
            Divider()
            SpacerM()
            LocationHandler(
                isLoading = state.locationIsLoading,
                locationResult = state.locationResult,
                onFetchLocationClick = onFetchLocationClick,
                onRequestLocationPermissionClick = onRequestLocationPermissionClick,
                permissionGranted = locationPermissionGranted,
            )
        }
    }
}

@Composable
fun LocationHandler(
    isLoading: Boolean,
    locationResult: Result<PieceLocation, LocationResultException>?,
    onFetchLocationClick: () -> Unit,
    onRequestLocationPermissionClick: () -> Unit,
    permissionGranted : Boolean,
) {
    Row(
        modifier = Modifier.row(),
        horizontalArrangement = Arrangement.Center,
    ) {
        if (locationResult != null) { // location is being used
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.circularProgressIndicator())
            } else locationResult.mapBoth(
                { location ->
                    Text(text = location.toString())
                },
                { error ->
                    when(error) {
                        LocationResultException.PermissionRequestFlow -> {
                            if (permissionGranted)
                                onFetchLocationClick()
                            else
                                Button(
                                    onClick = { onRequestLocationPermissionClick() },
                                ) {
                                    Text(text = stringResource(id = AppText.grant_location_permission))
                                }
                        }
                        LocationResultException.LocationFetchException -> {
                            Button(
                                onClick = { onFetchLocationClick() },
                            ) {
                                Text(text = stringResource(id = AppText.error_try_again))
                            }
                        }
                    }
                }
            )
        } else { // location is not used
            Button(
                onClick = { onFetchLocationClick() },
            ) {
                Text(text = stringResource(id = AppText.set_location))
            }
        }
    }
}

