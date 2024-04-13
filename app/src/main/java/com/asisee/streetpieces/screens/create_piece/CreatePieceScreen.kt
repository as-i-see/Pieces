package com.asisee.streetpieces.screens.create_piece

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

@OptIn(ExperimentalPermissionsApi::class)
@Destination(navArgsDelegate = CreatePieceScreenNavArgs::class)
@Composable
fun CreatePieceScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePieceViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val locationPermissionState = rememberMultiplePermissionsState(listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ))

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            CreatePieceSideEffect.NavigateFurther -> {
                navigator.navigate(SearchFeedScreenDestination)
            }

            CreatePieceSideEffect.MissingTitleError -> {
                SnackbarManager.showMessage(AppText.title_error_message)
            }

            CreatePieceSideEffect.LocationStillLoadingError -> {
                SnackbarManager.showMessage(AppText.location_error_message)
            }

            CreatePieceSideEffect.RequestLocationPermission -> {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
    }



    CreatePieceView(
        state = state,
        onDoneClick = { viewModel.onDoneClick() },
        onTitleChange = { newString -> viewModel.onTitleChange(newString) },
        onFetchLocationClick = { viewModel.fetchLocation() },
        onRequestLocationPermissionClick = { viewModel.requestLocationPermission() },
        locationPermissionGranted = locationPermissionState.allPermissionsGranted,
    )
}
