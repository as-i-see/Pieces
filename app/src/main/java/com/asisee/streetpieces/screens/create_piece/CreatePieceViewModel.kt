package com.asisee.streetpieces.screens.create_piece

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import com.asisee.streetpieces.common.ext.toPieceLocation
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LocationService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.screens.LogViewModel
import com.asisee.streetpieces.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.Clock
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class CreatePieceViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: PieceStorageService,
    private val photoStorageService: PhotoStorageService,
    private val accountService: AccountService,
    private val locationService: LocationService
) : ContainerHost<CreatePieceState, CreatePieceSideEffect>, LogViewModel(logService) {
    private val navArgs: CreatePieceScreenNavArgs = savedStateHandle.navArgs()
    override val container: Container<CreatePieceState, CreatePieceSideEffect> =
        container(CreatePieceState(Piece(photoUri = navArgs.photoUri)), savedStateHandle)

    fun onTitleChange(newTitle: String) = intent {
        reduce {
            state.copy(piece = state.piece.copy(title = newTitle))
        }
    }

    fun onDoneClick() = intent {
        if (state.piece.title.isBlank()) {
            postSideEffect(CreatePieceSideEffect.MissingTitleError)
        } else if (state.usingLocation && state.piece.location == null) {
            reduce {
                state.copy(usingLocation = false)
            }
            postSideEffect(CreatePieceSideEffect.LocationFetchError)
        } else launchCatching {
            reduce {
                state.copy(showLoader = true)
            }
            val resultPiece = state.piece.copy(
                dateTimeInEpochSeconds = Clock.System.now().epochSeconds,
                photoUri = photoStorageService.uploadPiecePhoto(state.piece.photoUri.toUri())
            )
            storageService.save(resultPiece)
            postSideEffect(CreatePieceSideEffect.NavigateFurther)
        }
    }
    fun fetchLocation() = intent {
        reduce {
            state.copy(usingLocation = true)
        }
        launchCatching(false) {
            val currentLocation = locationService.getCurrentLocation().toPieceLocation()
            reduce {
                state.copy(piece = state.piece.copy(location = currentLocation))
            }
        }
    }
}
