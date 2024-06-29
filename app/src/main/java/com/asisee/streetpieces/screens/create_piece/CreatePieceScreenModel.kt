package com.asisee.streetpieces.screens.create_piece

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.common.ext.toPieceLocation
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LocationService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.PostService
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.runOn

@Factory
@OptIn(OrbitExperimental::class)
class CreatePieceScreenModel (
    private val storageService: PostService,
    private val photoStorageService: PhotoStorageService,
    private val accountService: AccountService,
    private val locationService: LocationService
) : ScreenModel, ContainerHost<CreatePieceScreenState, CreatePieceSideEffect> {
    override val container =
        screenModelScope.container<CreatePieceScreenState, CreatePieceSideEffect>(CreatePieceScreenState.Loading)

    fun onTitleChange(newTitle: String) = intent {
        runOn(CreatePieceScreenState.CreatePiece::class) {
            reduce {
                state.copy(postData = state.postData.copy(title = newTitle))
            }
        }
    }

    fun onDoneClick() = intent {
        (state as? CreatePieceScreenState.CreatePiece)?.let { state ->
            if (state.postData.title.isBlank()) {
                postSideEffect(CreatePieceSideEffect.MissingTitleError)
            } else if (state.pieceLocation is CreatePieceScreenState.PieceLocationState.Loading)
                postSideEffect(CreatePieceSideEffect.LocationStillLoadingError)
            else {
                reduce {
                    CreatePieceScreenState.Loading
                }
                screenModelScope.launch(
                    CoroutineExceptionHandler { _, _ ->
                        intent {
                            postSideEffect(CreatePieceSideEffect.UploadError)
                            reduce {
                                state
                            }
                        }
                    }
                ) {
                    val uploadedPhotoUri = photoStorageService.uploadPiecePhoto(state.postData.pictureUrl)
                    val pieceToUpload = state.postData.copy(
                        epochSecondsCreatedAt = Clock.System.now().epochSeconds,
                        pictureUrl = uploadedPhotoUri
                    ).let { pieceData ->
                        if (state.pieceLocation is CreatePieceScreenState.PieceLocationState.Used) {
                            pieceData.copy(
                                location = state.pieceLocation.pieceLocation
                            )
                        } else {
                            pieceData
                        }
                    }
                    storageService.save(pieceToUpload)
                    postSideEffect(CreatePieceSideEffect.NavigateFurther)
                }
            }
        }
    }
    fun fetchLocation() = intent {
        runOn(CreatePieceScreenState.CreatePiece::class) {
            reduce {
                state.copy(pieceLocation = CreatePieceScreenState.PieceLocationState.Loading)
            }
            val locationResultState = locationService.getCurrentLocation().map { it.toPieceLocation() }.mapBoth(
                { pieceLocation -> CreatePieceScreenState.PieceLocationState.Used(pieceLocation)},
                { error -> CreatePieceScreenState.PieceLocationState.Error(error)}
            )
            reduce {
                state.copy(pieceLocation = locationResultState)
            }
        }
    }

    fun requestLocationPermission() = intent {
        postSideEffect(CreatePieceSideEffect.RequestLocationPermission)
    }

    fun navBack() = intent {
        postSideEffect(CreatePieceSideEffect.NavBack)
    }
}
