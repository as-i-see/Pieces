package com.asisee.streetpieces.screens.camera

import android.net.Uri
import android.os.Build
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.CREATE_PIECE_SCREEN
import com.asisee.streetpieces.PHOTO_URI
import com.asisee.streetpieces.model.datasource.FileDataSource
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageCaptureResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    logService: LogService,
    private val fileDataSource: FileDataSource,
    ) : LogViewModel(logService) {

    private val _uiState: MutableStateFlow<CameraUiState> = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> get() = _uiState

    fun takePicture(openScreen: (String) -> Unit, cameraState: CameraState) = with(cameraState) {
        viewModelScope.launch {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> takePicture(
                    fileDataSource.imageContentValues,
                ) {
                    onImageResult(openScreen, it)
                }

                else -> takePicture(
                    fileDataSource.getFile("jpg")
                ) {
                    onImageResult(openScreen, it)
                }
            }
        }
    }

    private fun captureSuccess(openScreen: (String) -> Unit, photoUri: Uri) {
        openScreen("$CREATE_PIECE_SCREEN?$PHOTO_URI={$photoUri}")
//        viewModelScope.launch {
//            _uiState.update {
//                CameraUiState()
//            }
//        }
    }

    private fun onImageResult(openScreen: (String) -> Unit, imageResult: ImageCaptureResult) {
        when (imageResult) {
            is ImageCaptureResult.Error -> onError(imageResult.throwable)
            is ImageCaptureResult.Success -> launchCatching {
                captureSuccess(openScreen, imageResult.savedUri!!)
            }
        }
    }

    private fun onError(throwable: Throwable?) {
        _uiState.update { CameraUiState(throwable) }
    }

    fun onGalleryClick() = Unit // TODO
}