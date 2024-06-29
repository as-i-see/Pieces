package com.asisee.streetpieces.screens.camera

sealed interface CameraScreenSideEffect {
    data class PhotoResult(val photoUri: String) : CameraScreenSideEffect
    data class Error(val message: String) : CameraScreenSideEffect
}