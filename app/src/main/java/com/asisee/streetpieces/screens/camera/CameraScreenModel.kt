package com.asisee.streetpieces.screens.camera

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.R
import com.asisee.streetpieces.model.datasource.FileDataSource
import com.ujizin.camposer.state.CameraState
import com.ujizin.camposer.state.ImageCaptureResult
import dagger.hilt.android.qualifiers.ApplicationContext
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Factory
class CameraScreenModel (
    private val fileDataSource: FileDataSource,
) : ScreenModel, ContainerHost<CameraScreenState, CameraScreenSideEffect> {

    override val container = screenModelScope.container<CameraScreenState, CameraScreenSideEffect>(CameraScreenState)

    fun takePicture(cameraState: CameraState) = intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cameraState.takePicture(fileDataSource.imageContentValues) {
                onImageResult(it)
            }
        } else {
            cameraState.takePicture(fileDataSource.getFile("jpg")) {
                onImageResult(it)
            }
        }
    }

    private fun onImageResult(imageResult: ImageCaptureResult) = intent {
        postSideEffect(when (imageResult) {
            is ImageCaptureResult.Error ->
                CameraScreenSideEffect.Error(imageResult.throwable.message ?: "Unknown error")
            is ImageCaptureResult.Success ->
                CameraScreenSideEffect.PhotoResult(imageResult.savedUri.toString())
        })
    }

    fun openGallery() = Unit // TODO
}