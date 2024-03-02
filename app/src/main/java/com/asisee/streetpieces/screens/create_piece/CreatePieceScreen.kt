package com.asisee.streetpieces.screens.create_piece

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

@Destination(navArgsDelegate = CreatePieceScreenNavArgs::class)
@Composable
fun CreatePieceScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePieceViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            CreatePieceSideEffect.NavigateFurther -> {
                navigator.navigate(SearchFeedScreenDestination)
            }

            CreatePieceSideEffect.MissingTitleError -> {
                SnackbarManager.showMessage(AppText.title_error_message)
            }

            CreatePieceSideEffect.LocationFetchError -> {
                SnackbarManager.showMessage(AppText.location_error_message)
            }
        }
    }

    CreatePieceView(
        state = state,
        onDoneClick = { viewModel.onDoneClick() },
        onTitleChange = { newString -> viewModel.onTitleChange(newString) },
        onFetchLocationClick = { viewModel.fetchLocation() }
    )
}
