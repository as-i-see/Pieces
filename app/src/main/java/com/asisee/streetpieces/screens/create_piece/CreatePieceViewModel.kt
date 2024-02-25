package com.asisee.streetpieces.screens.create_piece

import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.PieceLocation
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LocationService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.screens.LogViewModel
import com.asisee.streetpieces.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
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
) : LogViewModel(logService) {
    private val navArgs: CreatePieceScreenNavArgs = savedStateHandle.navArgs()
    val piece = mutableStateOf(Piece(photoUri = navArgs.photoUri))
    var locationFetchTick = 0
    private var locationFetchJob: Job =
        viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000)
                locationFetchTick++
            }
        }

    fun onTitleChange(newValue: String) {
        piece.value = piece.value.copy(title = newValue)
    }

    fun onDoneClick(toMainScreen: () -> Unit) {
        launchCatching {
            val pieceDto =
                piece.value.copy(
                    dateTimeInEpochSeconds = Clock.System.now().epochSeconds,
                    photoUri = photoStorageService.uploadPiecePhoto(piece.value.photoUri.toUri()))
            storageService.save(pieceDto)
            toMainScreen()
        }
    }

    fun updateLocation() {
        launchCatching(false) {
            piece.value =
                piece.value.copy(
                    location =
                        locationService.getCurrentLocation().run {
                            PieceLocation(latitude, longitude)
                        })
        }
    }

    fun onLocationFetched() {
        if (locationFetchJob.isActive) locationFetchJob.cancel()
    }
}
