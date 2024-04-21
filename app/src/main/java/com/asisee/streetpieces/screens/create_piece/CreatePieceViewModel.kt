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
import com.github.michaelbull.result.map
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
) : LogViewModel(logService), ContainerHost<CreatePieceState, CreatePieceSideEffect> {
    private val navArgs: CreatePieceScreenNavArgs = savedStateHandle.navArgs()
    override val container: Container<CreatePieceState, CreatePieceSideEffect> =
        container(CreatePieceState(Piece(photoUri = navArgs.photoUri)))

    fun onTitleChange(newTitle: String) = intent {
        reduce {
            state.copy(piece = state.piece.copy(title = newTitle))
        }
    }

    fun onDoneClick() = intent {
        if (state.piece.title.isBlank()) {
            postSideEffect(CreatePieceSideEffect.MissingTitleError)
        } else if (state.locationIsLoading) {
            postSideEffect(CreatePieceSideEffect.LocationStillLoadingError)
        } else launchCatching {
            reduce {
                state.copy(pieceIsUploading = true)
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
            state.copy(
                locationIsLoading = true
            )
        }
        val location = locationService.getCurrentLocation().map { it.toPieceLocation() }
        reduce {
            state.copy(
                locationIsLoading = false,
                locationResult = location
            )
        }
    }

    fun requestLocationPermission() = intent {
        postSideEffect(CreatePieceSideEffect.RequestLocationPermission)
    }
}
