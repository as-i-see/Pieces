package com.asisee.streetpieces.screens.create_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.common.ext.toPieceLocation
import com.asisee.streetpieces.model.PostData
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
import org.koin.core.annotation.InjectedParam
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.runOn

@Factory
@OptIn(OrbitExperimental::class)
class CreatePostScreenModel (
    @InjectedParam pictureUri: String,
    private val storageService: PostService,
    private val photoStorageService: PhotoStorageService,
    private val accountService: AccountService,
    private val locationService: LocationService
) : ScreenModel, ContainerHost<CreatePostScreenState, CreatePostSideEffect> {

    override val container = screenModelScope.container<CreatePostScreenState, CreatePostSideEffect>(
        CreatePostScreenState.CreatePost(postData = PostData(pictureUrl = pictureUri))
    )

    var title by mutableStateOf("")
        private set

    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onDoneClick() = intent {
        (state as? CreatePostScreenState.CreatePost)?.let { state ->
            if (title.isBlank()) {
                postSideEffect(CreatePostSideEffect.MissingTitleError)
            } else if (state.pieceLocation is CreatePostScreenState.PieceLocationState.Loading)
                postSideEffect(CreatePostSideEffect.LocationStillLoadingError)
            else {
                reduce {
                    CreatePostScreenState.Loading
                }
                screenModelScope.launch(
                    CoroutineExceptionHandler { _, _ ->
                        intent {
                            postSideEffect(CreatePostSideEffect.UploadError)
                            reduce {
                                state
                            }
                        }
                    }
                ) {
                    val uploadedPhotoUri = photoStorageService.uploadPiecePhoto(state.postData.pictureUrl)
                    val pieceToUpload = state.postData.copy(
                        title = title,
                        epochSecondsCreatedAt = Clock.System.now().epochSeconds,
                        pictureUrl = uploadedPhotoUri
                    ).let { pieceData ->
                        if (state.pieceLocation is CreatePostScreenState.PieceLocationState.Used) {
                            pieceData.copy(
                                location = state.pieceLocation.pieceLocation
                            )
                        } else {
                            pieceData
                        }
                    }
                    storageService.save(pieceToUpload)
                    postSideEffect(CreatePostSideEffect.NavigateToProfile)
                }
            }
        }
    }
    fun fetchLocation() = intent {
        runOn(CreatePostScreenState.CreatePost::class) {
            reduce {
                state.copy(pieceLocation = CreatePostScreenState.PieceLocationState.Loading)
            }
            val locationResultState = locationService.getCurrentLocation().map { it.toPieceLocation() }.mapBoth(
                { pieceLocation -> CreatePostScreenState.PieceLocationState.Used(pieceLocation)},
                { error -> CreatePostScreenState.PieceLocationState.Error(error)}
            )
            reduce {
                state.copy(pieceLocation = locationResultState)
            }
        }
    }

    fun requestLocationPermission() = intent {
        postSideEffect(CreatePostSideEffect.RequestLocationPermission)
    }

    fun navBack() = intent {
        postSideEffect(CreatePostSideEffect.NavBack)
    }
}
